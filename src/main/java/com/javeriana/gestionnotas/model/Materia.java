package com.javeriana.gestionnotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Materia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Column(nullable = false)
    private Integer creditos;
    
    @Column(length = 500)
    private String descripcion;
    
    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
}