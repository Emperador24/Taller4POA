package com.javeriana.gestionnotas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.javeriana.gestionnotas.model.Materia;
import com.javeriana.gestionnotas.model.Nota;
import com.javeriana.gestionnotas.model.Usuario;
import com.javeriana.gestionnotas.repository.MateriaRepository;
import com.javeriana.gestionnotas.repository.NotaRepository;
import com.javeriana.gestionnotas.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    @Autowired
    private NotaRepository notaRepository;
    
    @Override
    public void run(String... args) throws Exception {
        
        // Crear profesores
        Usuario profesor1 = new Usuario();
        profesor1.setNombre("Jorge Valenzuela");
        profesor1.setEmail("jorge.valenzuela@javeriana.edu.co");
        profesor1.setContrasena("profesor123");
        profesor1.setRol("Profesor");
        usuarioRepository.save(profesor1);
        
        // Crear alumnos
        Usuario alumno1 = new Usuario();
        alumno1.setNombre("Juan Pérez");
        alumno1.setEmail("juan.perez@javeriana.edu.co");
        alumno1.setContrasena("alumno123");
        alumno1.setRol("Alumno");
        alumno1.setCodigoEstudiante("EST001");
        usuarioRepository.save(alumno1);

        // Crear materias
        Materia materia1 = new Materia();
        materia1.setNombre("Teoría de la Computación");
        materia1.setCreditos(3);
        materia1.setDescripcion("Fundamentos teóricos de la computación");
        materiaRepository.save(materia1);
        
        Materia materia3 = new Materia();
        materia3.setNombre("Bases de Datos");
        materia3.setCreditos(4);
        materia3.setDescripcion("Diseño e implementación de bases de datos");
        materiaRepository.save(materia3);
        
        // Crear notas para alumno1 en materia1
        Nota nota1 = new Nota();
        nota1.setEstudiante(alumno1);
        nota1.setMateria(materia1);
        nota1.setDescripcion("Primer Parcial");
        nota1.setValor(4.5);
        nota1.setPorcentaje(30);
        notaRepository.save(nota1);
        
        Nota nota2 = new Nota();
        nota2.setEstudiante(alumno1);
        nota2.setMateria(materia1);
        nota2.setDescripcion("Segundo Parcial");
        nota2.setValor(4.0);
        nota2.setPorcentaje(30);
        notaRepository.save(nota2);
        
        System.out.println("===================================");
        System.out.println("Datos iniciales cargados exitosamente");
        System.out.println("===================================");
        System.out.println("USUARIOS DE PRUEBA:");
        System.out.println("Profesor: jorge.valenzuela@javeriana.edu.co / profesor123");
        System.out.println("Alumno 1: juan.perez@javeriana.edu.co / alumno123");
        System.out.println("===================================");
    }
}