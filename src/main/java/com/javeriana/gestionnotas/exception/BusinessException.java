package com.javeriana.gestionnotas.exception;

// Excepción base
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Excepción de recurso no encontrado
class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// Excepción de validación
class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(message);
    }
}

// Excepción de autorización
class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

// Excepción de porcentaje
class PorcentajeExceededException extends BusinessException {
    public PorcentajeExceededException(String message) {
        super(message);
    }
}