package com.javeriana.gestionnotas.config;

import com.javeriana.gestionnotas.model.Usuario;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionManager implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Usuario usuarioActual;
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuario getUsuarioActual() {
        return this.usuarioActual;
    }
    
    public boolean isAuthenticated() {
        return this.usuarioActual != null;
    }
    
    public void clear() {
        this.usuarioActual = null;
    }
    
    public String getRolActual() {
        return this.usuarioActual != null ? this.usuarioActual.getRol() : null;
    }
    
    public Long getIdUsuarioActual() {
        return this.usuarioActual != null ? this.usuarioActual.getId() : null;
    }
    
    public boolean esAlumno() {
        return this.usuarioActual != null && this.usuarioActual.esAlumno();
    }
    
    public boolean esProfesor() {
        return this.usuarioActual != null && this.usuarioActual.esProfesor();
    }
}