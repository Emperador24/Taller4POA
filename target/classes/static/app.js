const API_URL = 'http://localhost:8080/api';
let currentUser = null;
let usuarios = [];
let materias = [];
let notas = [];

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    checkSession();
    setupForms();
});

// Verificar sesión
async function checkSession() {
    try {
        const response = await fetch(`${API_URL}/auth/session`);
        const data = await response.json();
        
        if (data.authenticated) {
            currentUser = data.usuario;
            showMainScreen();
        }
    } catch (error) {
        console.error('Error verificando sesión:', error);
    }
}

// Login
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = document.getElementById('loginEmail').value;
    const contrasena = document.getElementById('loginPassword').value;
    
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, contrasena })
        });
        
        const data = await response.json();
        
        if (data.success) {
            currentUser = data.usuario;
            showMainScreen();
        } else {
            showLoginError(data.message);
        }
    } catch (error) {
        showLoginError('Error de conexión con el servidor');
    }
});

function showLoginError(message) {
    const errorDiv = document.getElementById('loginError');
    errorDiv.textContent = message;
    errorDiv.classList.remove('hidden');
}

// Logout
async function logout() {
    try {
        await fetch(`${API_URL}/auth/logout`, { method: 'POST' });
        currentUser = null;
        document.getElementById('mainScreen').classList.add('hidden');
        document.getElementById('loginScreen').classList.remove('hidden');
        document.getElementById('loginForm').reset();
    } catch (error) {
        console.error('Error en logout:', error);
    }
}

// Mostrar pantalla principal
function showMainScreen() {
    document.getElementById('loginScreen').classList.add('hidden');
    document.getElementById('mainScreen').classList.remove('hidden');
    
    document.getElementById('userName').textContent = currentUser.nombre;
    const roleBadge = document.getElementById('userRole');
    roleBadge.textContent = currentUser.rol;
    roleBadge.className = `badge badge-${currentUser.rol.toLowerCase()}`;
    
    // Ocultar tabs según rol
    if (currentUser.rol === 'Alumno') {
        document.getElementById('tabMaterias').classList.add('hidden');
        document.getElementById('tabUsuarios').classList.add('hidden');
        document.getElementById('btnCreateNota').classList.add('hidden');
        document.querySelector('.actions-column').classList.add('hidden');
    }
    
    loadData();
}

// Cargar datos
async function loadData() {
    await Promise.all([
        loadUsuarios(),
        loadMaterias(),
        loadNotas()
    ]);
}

async function loadUsuarios() {
    try {
        const response = await fetch(`${API_URL}/usuarios`);
        usuarios = await response.json();
        renderUsuarios();
    } catch (error) {
        showError('Error cargando usuarios');
    }
}

async function loadMaterias() {
    try {
        const response = await fetch(`${API_URL}/materias`);
        materias = await response.json();
        renderMaterias();
        populateMateriaSelect();
    } catch (error) {
        showError('Error cargando materias');
    }
}

async function loadNotas() {
    try {
        const response = await fetch(`${API_URL}/notas`);
        notas = await response.json();
        renderNotas();
        if (currentUser.rol === 'Alumno') {
            loadPromedio();
        }
    } catch (error) {
        showError('Error cargando notas');
    }
}

// Renderizar tablas
function renderNotas() {
    const tbody = document.querySelector('#notasTable tbody');
    tbody.innerHTML = '';
    
    notas.forEach(nota => {
        const ponderado = (nota.valor * nota.porcentaje / 100).toFixed(2);
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${nota.id}</td>
            <td>${nota.estudiante?.nombre || 'N/A'}</td>
            <td>${nota.materia?.nombre || 'N/A'}</td>
            <td>${nota.descripcion}</td>
            <td>${nota.valor.toFixed(1)}</td>
            <td>${nota.porcentaje}%</td>
            <td>${ponderado}</td>
            <td class="actions ${currentUser.rol === 'Alumno' ? 'hidden' : ''}">
                <button class="btn btn-primary btn-sm" onclick="editNota(${nota.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deleteNota(${nota.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function renderMaterias() {
    const tbody = document.querySelector('#materiasTable tbody');
    tbody.innerHTML = '';
    
    materias.forEach(materia => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${materia.id}</td>
            <td>${materia.nombre}</td>
            <td>${materia.creditos}</td>
            <td>${materia.descripcion || ''}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editMateria(${materia.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deleteMateria(${materia.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function renderUsuarios() {
    const tbody = document.querySelector('#usuariosTable tbody');
    tbody.innerHTML = '';
    
    usuarios.forEach(usuario => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${usuario.id}</td>
            <td>${usuario.nombre}</td>
            <td>${usuario.email}</td>
            <td><span class="badge badge-${usuario.rol.toLowerCase()}">${usuario.rol}</span></td>
            <td>${usuario.codigoEstudiante || '-'}</td>
            <td>
                <button class="btn btn-primary btn-sm" onclick="editUsuario(${usuario.id})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="deleteUsuario(${usuario.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Tabs
function showTab(tabName) {
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    
    event.target.classList.add('active');
    document.getElementById(`${tabName}Tab`).classList.add('active');
    
    if (tabName === 'promedio') {
        loadPromedio();
    }
}

// Promedio
async function loadPromedio() {
    try {
        const response = await fetch(`${API_URL}/notas/promedio/${currentUser.id}`);
        const data = await response.json();
        
        const content = document.getElementById('promedioContent');
        content.innerHTML = `
            <div style="text-align: center; padding: 40px;">
                <h3 style="color: #666; margin-bottom: 10px;">Tu Promedio General</h3>
                <div style="font-size: 72px; font-weight: bold; color: ${data.promedio >= 3.0 ? '#27ae60' : '#e74c3c'};">
                    ${data.promedio.toFixed(2)}
                </div>
                <p style="color: #999; margin-top: 10px;">Sobre 5.0</p>
            </div>
        `;
        
        // Mostrar notas por materia
        const materiaIds = [...new Set(notas.map(n => n.materia.id))];
        let materiasHtml = '<h3 style="margin-top: 30px;">Notas por Materia</h3>';
        
        for (const materiaId of materiaIds) {
            const notasMateria = notas.filter(n => n.materia.id === materiaId);
            const materia = notasMateria[0].materia;
            const acumulado = notasMateria.reduce((sum, n) => sum + (n.valor * n.porcentaje / 100), 0);
            const porcentajeTotal = notasMateria.reduce((sum, n) => sum + n.porcentaje, 0);
            
            materiasHtml += `
                <div style="background: #f8f9fa; padding: 15px; border-radius: 5px; margin-top: 15px;">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <div>
                            <h4 style="color: #333;">${materia.nombre}</h4>
                            <p style="color: #666; font-size: 14px;">Porcentaje completado: ${porcentajeTotal}%</p>
                        </div>
                        <div style="text-align: right;">
                            <div style="font-size: 32px; font-weight: bold; color: ${acumulado >= 3.0 ? '#27ae60' : '#e74c3c'};">
                                ${acumulado.toFixed(2)}
                            </div>
                            <p style="color: #999; font-size: 12px;">Nota acumulada</p>
                        </div>
                    </div>
                </div>
            `;
        }
        
        content.innerHTML += materiasHtml;
    } catch (error) {
        showError('Error cargando promedio');
    }
}

// Modals - Notas
function showCreateNotaModal() {
    document.getElementById('notaModalTitle').textContent = 'Crear Nota';
    document.getElementById('notaForm').reset();
    document.getElementById('notaId').value = '';
    populateEstudianteSelect();
    populateMateriaSelect();
    document.getElementById('notaModal').classList.add('active');
}

async function editNota(id) {
    const nota = notas.find(n => n.id === id);
    if (!nota) return;
    
    document.getElementById('notaModalTitle').textContent = 'Editar Nota';
    document.getElementById('notaId').value = nota.id;
    document.getElementById('notaEstudiante').value = nota.estudiante.id;
    document.getElementById('notaMateria').value = nota.materia.id;
    document.getElementById('notaDescripcion').value = nota.descripcion;
    document.getElementById('notaValor').value = nota.valor;
    document.getElementById('notaPorcentaje').value = nota.porcentaje;
    
    populateEstudianteSelect();
    populateMateriaSelect();
    document.getElementById('notaModal').classList.add('active');
}

function closeNotaModal() {
    document.getElementById('notaModal').classList.remove('active');
}

document.getElementById('notaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const id = document.getElementById('notaId').value;
    const estudianteId = document.getElementById('notaEstudiante').value;
    const materiaId = document.getElementById('notaMateria').value;
    
    const nota = {
        estudiante: { id: parseInt(estudianteId) },
        materia: { id: parseInt(materiaId) },
        descripcion: document.getElementById('notaDescripcion').value,
        valor: parseFloat(document.getElementById('notaValor').value),
        porcentaje: parseInt(document.getElementById('notaPorcentaje').value)
    };
    
    try {
        const url = id ? `${API_URL}/notas/${id}` : `${API_URL}/notas`;
        const method = id ? 'PUT' : 'POST';
        
        if (id) nota.id = parseInt(id);
        
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nota)
        });
        
        if (response.ok) {
            showSuccess(`Nota ${id ? 'actualizada' : 'creada'} exitosamente`);
            closeNotaModal();
            await loadNotas();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al guardar la nota');
        }
    } catch (error) {
        showError('Error de conexión');
    }
});

async function deleteNota(id) {
    if (!confirm('¿Está seguro de eliminar esta nota?')) return;
    
    try {
        const response = await fetch(`${API_URL}/notas/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            showSuccess('Nota eliminada exitosamente');
            await loadNotas();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al eliminar la nota');
        }
    } catch (error) {
        showError('Error de conexión');
    }
}

// Modals - Materias
function showCreateMateriaModal() {
    document.getElementById('materiaModalTitle').textContent = 'Crear Materia';
    document.getElementById('materiaForm').reset();
    document.getElementById('materiaId').value = '';
    document.getElementById('materiaModal').classList.add('active');
}

function editMateria(id) {
    const materia = materias.find(m => m.id === id);
    if (!materia) return;
    
    document.getElementById('materiaModalTitle').textContent = 'Editar Materia';
    document.getElementById('materiaId').value = materia.id;
    document.getElementById('materiaNombre').value = materia.nombre;
    document.getElementById('materiaCreditos').value = materia.creditos;
    document.getElementById('materiaDescripcion').value = materia.descripcion || '';
    document.getElementById('materiaModal').classList.add('active');
}

function closeMateriaModal() {
    document.getElementById('materiaModal').classList.remove('active');
}

document.getElementById('materiaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const id = document.getElementById('materiaId').value;
    const materia = {
        nombre: document.getElementById('materiaNombre').value,
        creditos: parseInt(document.getElementById('materiaCreditos').value),
        descripcion: document.getElementById('materiaDescripcion').value
    };
    
    try {
        const url = id ? `${API_URL}/materias/${id}` : `${API_URL}/materias`;
        const method = id ? 'PUT' : 'POST';
        
        if (id) materia.id = parseInt(id);
        
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(materia)
        });
        
        if (response.ok) {
            showSuccess(`Materia ${id ? 'actualizada' : 'creada'} exitosamente`);
            closeMateriaModal();
            await loadMaterias();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al guardar la materia');
        }
    } catch (error) {
        showError('Error de conexión');
    }
});

async function deleteMateria(id) {
    if (!confirm('¿Está seguro de eliminar esta materia?')) return;
    
    try {
        const response = await fetch(`${API_URL}/materias/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            showSuccess('Materia eliminada exitosamente');
            await loadMaterias();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al eliminar la materia');
        }
    } catch (error) {
        showError('Error de conexión');
    }
}

// Modals - Usuarios
function showCreateUsuarioModal() {
    document.getElementById('usuarioModalTitle').textContent = 'Crear Usuario';
    document.getElementById('usuarioForm').reset();
    document.getElementById('usuarioId').value = '';
    document.getElementById('usuarioModal').classList.add('active');
}

function editUsuario(id) {
    const usuario = usuarios.find(u => u.id === id);
    if (!usuario) return;
    
    document.getElementById('usuarioModalTitle').textContent = 'Editar Usuario';
    document.getElementById('usuarioId').value = usuario.id;
    document.getElementById('usuarioNombre').value = usuario.nombre;
    document.getElementById('usuarioEmail').value = usuario.email;
    document.getElementById('usuarioContrasena').value = usuario.contrasena;
    document.getElementById('usuarioRol').value = usuario.rol;
    document.getElementById('usuarioCodigoEstudiante').value = usuario.codigoEstudiante || '';
    toggleCodigoEstudiante();
    document.getElementById('usuarioModal').classList.add('active');
}

function closeUsuarioModal() {
    document.getElementById('usuarioModal').classList.remove('active');
}

function toggleCodigoEstudiante() {
    const rol = document.getElementById('usuarioRol').value;
    const codigoGroup = document.getElementById('codigoEstudianteGroup');
    codigoGroup.style.display = rol === 'Alumno' ? 'block' : 'none';
}

document.getElementById('usuarioForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const id = document.getElementById('usuarioId').value;
    const usuario = {
        nombre: document.getElementById('usuarioNombre').value,
        email: document.getElementById('usuarioEmail').value,
        contrasena: document.getElementById('usuarioContrasena').value,
        rol: document.getElementById('usuarioRol').value,
        codigoEstudiante: document.getElementById('usuarioCodigoEstudiante').value || null
    };
    
    try {
        const url = id ? `${API_URL}/usuarios/${id}` : `${API_URL}/usuarios`;
        const method = id ? 'PUT' : 'POST';
        
        if (id) usuario.id = parseInt(id);
        
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });
        
        if (response.ok) {
            showSuccess(`Usuario ${id ? 'actualizado' : 'creado'} exitosamente`);
            closeUsuarioModal();
            await loadUsuarios();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al guardar el usuario');
        }
    } catch (error) {
        showError('Error de conexión');
    }
});

async function deleteUsuario(id) {
    if (!confirm('¿Está seguro de eliminar este usuario?')) return;
    
    try {
        const response = await fetch(`${API_URL}/usuarios/${id}`, { method: 'DELETE' });
        
        if (response.ok) {
            showSuccess('Usuario eliminado exitosamente');
            await loadUsuarios();
        } else {
            const error = await response.json();
            showError(error.message || 'Error al eliminar el usuario');
        }
    } catch (error) {
        showError('Error de conexión');
    }
}

// Utilidades
function populateEstudianteSelect() {
    const select = document.getElementById('notaEstudiante');
    select.innerHTML = '<option value="">Seleccione un estudiante</option>';
    usuarios.filter(u => u.rol === 'Alumno').forEach(usuario => {
        select.innerHTML += `<option value="${usuario.id}">${usuario.nombre}</option>`;
    });
}

function populateMateriaSelect() {
    const select = document.getElementById('notaMateria');
    select.innerHTML = '<option value="">Seleccione una materia</option>';
    materias.forEach(materia => {
        select.innerHTML += `<option value="${materia.id}">${materia.nombre}</option>`;
    });
}

function setupForms() {
    // Ya configurados arriba con addEventListener
}

function showSuccess(message) {
    const alert = document.getElementById('successAlert');
    alert.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => alert.classList.add('hidden'), 3000);
}

function showError(message) {
    const alert = document.getElementById('errorAlert');
    alert.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => alert.classList.add('hidden'), 5000);
}