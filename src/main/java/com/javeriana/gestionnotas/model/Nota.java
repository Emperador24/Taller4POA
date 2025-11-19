package com.javeriana.gestionnotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
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
    
    public Double getValorPonderado() {
        return (this.valor * this.porcentaje) / 100.0;
    }
}