package com.javeriana.gestionnotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String rol; // "Alumno" o "Profesor"
    
    @Column(name = "codigo_estudiante", unique = true)
    private String codigoEstudiante; // Solo para alumnos
    
    public boolean esAlumno() {
        return "Alumno".equalsIgnoreCase(this.rol);
    }
    
    public boolean esProfesor() {
        return "Profesor".equalsIgnoreCase(this.rol);
    }
}