package com.javeriana.gestionnotas.service;

import com.javeriana.gestionnotas.model.*;
import com.javeriana.gestionnotas.repository.*;
import com.javeriana.gestionnotas.exception.PorcentajeExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotaService {
    
    @Autowired
    private NotaRepository notaRepository;
    
    public List<Nota> findAll() {
        return notaRepository.findAll();
    }
    
    public Optional<Nota> findById(Long id) {
        return notaRepository.findById(id);
    }
    
    public Nota save(Nota nota) {
        validarPorcentaje(nota);
        return notaRepository.save(nota);
    }
    
    public void deleteById(Long id) {
        notaRepository.deleteById(id);
    }
    
    public List<Nota> findByEstudianteId(Long estudianteId) {
        return notaRepository.findByEstudianteId(estudianteId);
    }
    
    public List<Nota> findByMateriaId(Long materiaId) {
        return notaRepository.findByMateriaId(materiaId);
    }
    
    public List<Nota> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId) {
        return notaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId);
    }
    
    private void validarPorcentaje(Nota nota) {
        Integer sumaPorcentajes;
        
        if (nota.getId() != null) {
            // Actualización - excluir la nota actual
            sumaPorcentajes = notaRepository.sumPorcentajesExceptCurrent(
                nota.getEstudiante().getId(), 
                nota.getMateria().getId(), 
                nota.getId()
            );
        } else {
            // Creación nueva
            sumaPorcentajes = notaRepository.sumPorcentajesByEstudianteAndMateria(
                nota.getEstudiante().getId(), 
                nota.getMateria().getId()
            );
        }
        
        if (sumaPorcentajes == null) {
            sumaPorcentajes = 0;
        }
        
        if (sumaPorcentajes + nota.getPorcentaje() > 100) {
            // USA LA EXCEPCIÓN PERSONALIZADA
            throw new PorcentajeExceededException(
                String.format("La suma de porcentajes no puede superar el 100%%. " +
                "Porcentaje actual: %d%%, intentando agregar: %d%%", 
                sumaPorcentajes, nota.getPorcentaje())
            );
        }
    }
    
    public Double calcularPromedioEstudiante(Long estudianteId) {
        List<Nota> notas = notaRepository.findByEstudianteId(estudianteId);
        
        if (notas.isEmpty()) {
            return 0.0;
        }
        
        // Agrupar por materia y calcular promedio ponderado por materia
        Map<Long, List<Nota>> notasPorMateria = notas.stream()
            .collect(Collectors.groupingBy(n -> n.getMateria().getId()));
        
        double sumaPromedios = 0.0;
        int cantidadMaterias = notasPorMateria.size();
        
        for (List<Nota> notasMateria : notasPorMateria.values()) {
            double sumaPonderada = notasMateria.stream()
                .mapToDouble(Nota::getValorPonderado)
                .sum();
            sumaPromedios += sumaPonderada;
        }
        
        return sumaPromedios / cantidadMaterias;
    }
    
    public Double calcularNotaAcumulada(Long estudianteId, Long materiaId) {
        List<Nota> notas = notaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId);
        
        if (notas.isEmpty()) {
            return 0.0;
        }
        
        return notas.stream()
            .mapToDouble(Nota::getValorPonderado)
            .sum();
    }
}