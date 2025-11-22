package com.javeriana.gestionnotas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "notas")
public class Nota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario estudiante;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "notas"})
    private Materia materia;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;
    
    @DecimalMin(value = "0.0", message = "La nota debe ser al menos 0.0")
    @DecimalMax(value = "5.0", message = "La nota no puede superar 5.0")
    @Column(nullable = false)
    private Double valor;
    
    @Min(value = 1, message = "El porcentaje debe ser al menos 1")
    @Max(value = 100, message = "El porcentaje no puede superar 100")
    @Column(nullable = false)
    private Integer porcentaje;
    
    // Constructores
    public Nota() {}
    
    public Nota(Long id, Usuario estudiante, Materia materia, String descripcion, Double valor, Integer porcentaje) {
        this.id = id;
        this.estudiante = estudiante;
        this.materia = materia;
        this.descripcion = descripcion;
        this.valor = valor;
        this.porcentaje = porcentaje;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }
    
    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    
    public Integer getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Integer porcentaje) { this.porcentaje = porcentaje; }
    
    // Método de cálculo
    public Double getValorPonderado() {
        return (this.valor * this.porcentaje) / 100.0;
    }
}