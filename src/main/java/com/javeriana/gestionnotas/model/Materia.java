package com.javeriana.gestionnotas.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "materias")
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
    @JsonIgnore
    private List<Nota> notas = new ArrayList<>();
    
    // Constructores
    public Materia() {}
    
    public Materia(Long id, String nombre, Integer creditos, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
        this.descripcion = descripcion;
        this.notas = new ArrayList<>();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public List<Nota> getNotas() { return notas; }
    public void setNotas(List<Nota> notas) { this.notas = notas; }
}