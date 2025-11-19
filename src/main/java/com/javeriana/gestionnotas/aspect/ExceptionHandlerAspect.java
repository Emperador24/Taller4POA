package com.javeriana.gestionnotas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlerAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAspect.class);
    
    @AfterThrowing(
        pointcut = "execution(* com.javeriana.gestionnotas.controller..*(..))",
        throwing = "exception"
    )
    public void logControllerException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Excepción en el controlador {}.{}(): {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getMessage(),
            exception);
    }
    
    @AfterThrowing(
        pointcut = "execution(* com.javeriana.gestionnotas.service..*(..))",
        throwing = "exception"
    )
    public void logServiceException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Excepción en el servicio {}.{}(): {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getMessage(),
            exception);
    }
}