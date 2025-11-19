package com.javeriana.gestionnotas.config;

import com.javeriana.gestionnotas.model.Materia;
import com.javeriana.gestionnotas.model.Nota;
import com.javeriana.gestionnotas.model.Usuario;
import com.javeriana.gestionnotas.repository.MateriaRepository;
import com.javeriana.gestionnotas.repository.NotaRepository;
import com.javeriana.gestionnotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        
        Usuario alumno2 = new Usuario();
        alumno2.setNombre("María García");
        alumno2.setEmail("maria.garcia@javeriana.edu.co");
        alumno2.setContrasena("alumno123");
        alumno2.setRol("Alumno");
        alumno2.setCodigoEstudiante("EST002");
        usuarioRepository.save(alumno2);
        
        // Crear materias
        Materia materia1 = new Materia();
        materia1.setNombre("Teoría de la Computación");
        materia1.setCreditos(3);
        materia1.setDescripcion("Fundamentos teóricos de la computación");
        materiaRepository.save(materia1);
        
        Materia materia2 = new Materia();
        materia2.setNombre("Programación Orientada a Aspectos");
        materia2.setCreditos(3);
        materia2.setDescripcion("Conceptos avanzados de POA");
        materiaRepository.save(materia2);
        
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
        
        Nota nota3 = new Nota();
        nota3.setEstudiante(alumno1);
        nota3.setMateria(materia1);
        nota3.setDescripcion("Trabajo Final");
        nota3.setValor(4.8);
        nota3.setPorcentaje(40);
        notaRepository.save(nota3);
        
        // Crear notas para alumno1 en materia2
        Nota nota4 = new Nota();
        nota4.setEstudiante(alumno1);
        nota4.setMateria(materia2);
        nota4.setDescripcion("Taller 1");
        nota4.setValor(4.2);
        nota4.setPorcentaje(25);
        notaRepository.save(nota4);
        
        Nota nota5 = new Nota();
        nota5.setEstudiante(alumno1);
        nota5.setMateria(materia2);
        nota5.setDescripcion("Taller 2");
        nota5.setValor(4.7);
        nota5.setPorcentaje(25);
        notaRepository.save(nota5);
        
        // Crear notas para alumno2
        Nota nota6 = new Nota();
        nota6.setEstudiante(alumno2);
        nota6.setMateria(materia1);
        nota6.setDescripcion("Primer Parcial");
        nota6.setValor(3.8);
        nota6.setPorcentaje(30);
        notaRepository.save(nota6);
        
        Nota nota7 = new Nota();
        nota7.setEstudiante(alumno2);
        nota7.setMateria(materia3);
        nota7.setDescripcion("Quiz 1");
        nota7.setValor(4.0);
        nota7.setPorcentaje(20);
        notaRepository.save(nota7);
        
        System.out.println("===================================");
        System.out.println("Datos iniciales cargados exitosamente");
        System.out.println("===================================");
        System.out.println("USUARIOS DE PRUEBA:");
        System.out.println("Profesor: jorge.valenzuela@javeriana.edu.co / profesor123");
        System.out.println("Alumno 1: juan.perez@javeriana.edu.co / alumno123");
        System.out.println("Alumno 2: maria.garcia@javeriana.edu.co / alumno123");
        System.out.println("===================================");
    }
}