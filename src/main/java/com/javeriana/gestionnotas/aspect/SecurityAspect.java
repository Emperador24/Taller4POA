package com.javeriana.gestionnotas.aspect;

import com.javeriana.gestionnotas.config.SessionManager;
import com.javeriana.gestionnotas.model.Nota;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SecurityAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);
    
    @Autowired
    private SessionManager sessionManager;
    
    @Autowired
    private HttpServletRequest request;
    
    @Before("execution(* com.javeriana.gestionnotas.controller.NotaController.*(..))")
    public void verificarAccesoNotas(JoinPoint joinPoint) {
        
        if (!sessionManager.isAuthenticated()) {
            logger.warn("Intento de acceso sin autenticación al método: {}", joinPoint.getSignature().getName());
            throw new RuntimeException("Debe iniciar sesión para acceder a este recurso");
        }
        
        String metodo = request.getMethod();
        String nombreMetodo = joinPoint.getSignature().getName();
        
        logger.info("Usuario: {} (Rol: {}) accediendo al método: {} con HTTP {}",
            sessionManager.getUsuarioActual().getEmail(),
            sessionManager.getRolActual(),
            nombreMetodo,
            metodo);
        
        // Si es alumno
        if (sessionManager.esAlumno()) {
            
            // Solo permitir operaciones GET (lectura)
            if (!metodo.equals("GET") && !nombreMetodo.equals("findAll") && 
                !nombreMetodo.equals("findById") && !nombreMetodo.equals("findByEstudiante")) {
                
                logger.error("VIOLACIÓN DE SEGURIDAD: Alumno {} intentó realizar operación no permitida: {} {}",
                    sessionManager.getUsuarioActual().getEmail(),
                    metodo,
                    nombreMetodo);
                
                throw new RuntimeException("Los alumnos solo pueden consultar sus propias notas");
            }
            
            // Si está consultando una nota específica, verificar que sea suya
            if (nombreMetodo.equals("findById")) {
                Object[] args = joinPoint.getArgs();
                if (args.length > 0 && args[0] instanceof Long) {
                    // Esta validación se complementará en el controlador
                    logger.debug("Alumno consultando nota ID: {}", args[0]);
                }
            }
        }
        
        // Los profesores tienen acceso completo, solo registrar en log
        if (sessionManager.esProfesor()) {
            logger.info("Profesor {} realizando operación: {} {}",
                sessionManager.getUsuarioActual().getEmail(),
                metodo,
                nombreMetodo);
        }
    }
}