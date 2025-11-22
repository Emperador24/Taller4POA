package com.javeriana.gestionnotas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @Email(message = "Email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String contrasena;
    
    @Column(nullable = false)
    private String rol;
    
    @Column(name = "codigo_estudiante", unique = true)
    private String codigoEstudiante;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(Long id, String nombre, String email, String contrasena, String rol, String codigoEstudiante) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.codigoEstudiante = codigoEstudiante;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    
    // Métodos de negocio
    public boolean esAlumno() {
        return "Alumno".equalsIgnoreCase(this.rol);
    }
    
    public boolean esProfesor() {
        return "Profesor".equalsIgnoreCase(this.rol);
    }
}