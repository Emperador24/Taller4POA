package com.javeriana.gestionnotas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Around("execution(* com.javeriana.gestionnotas.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.debug("Ejecutando servicio: {}.{}() con argumentos: {}",
            className, methodName, Arrays.toString(joinPoint.getArgs()));
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            logger.debug("Servicio {}.{}() ejecutado exitosamente en {} ms",
                className, methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            logger.error("Error en servicio {}.{}(): {}",
                className, methodName, e.getMessage());
            throw e;
        }
    }
    
    @Before("execution(* com.javeriana.gestionnotas.controller.*.*(..))")
    public void logControllerEntry(JoinPoint joinPoint) {
        logger.info(">>> Entrando al controlador: {}.{}()",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());
    }
    
    @AfterReturning(
        pointcut = "execution(* com.javeriana.gestionnotas.controller.*.*(..))",
        returning = "result"
    )
    public void logControllerExit(JoinPoint joinPoint, Object result) {
        logger.info("<<< Saliendo del controlador: {}.{}() con resultado: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            result != null ? result.getClass().getSimpleName() : "null");
    }
}