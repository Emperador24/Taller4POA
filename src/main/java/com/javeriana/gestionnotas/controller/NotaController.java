package com.javeriana.gestionnotas.controller;

import com.javeriana.gestionnotas.config.SessionManager;
import com.javeriana.gestionnotas.model.Nota;
import com.javeriana.gestionnotas.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas")
public class NotaController {
    
    @Autowired
    private NotaService notaService;
    
    @Autowired
    private SessionManager sessionManager;
    
    @GetMapping
    public ResponseEntity<List<Nota>> findAll() {
        // Si es alumno, solo mostrar sus notas
        if (sessionManager.esAlumno()) {
            Long estudianteId = sessionManager.getIdUsuarioActual();
            return ResponseEntity.ok(notaService.findByEstudianteId(estudianteId));
        }
        // Si es profesor, mostrar todas
        return ResponseEntity.ok(notaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Nota> findById(@PathVariable Long id) {
        Optional<Nota> nota = notaService.findById(id);
        
        if (nota.isPresent()) {
            // Si es alumno, verificar que la nota le pertenezca
            if (sessionManager.esAlumno()) {
                if (!nota.get().getEstudiante().getId().equals(sessionManager.getIdUsuarioActual())) {
                    throw new RuntimeException("No tiene permisos para ver esta nota");
                }
            }
            return ResponseEntity.ok(nota.get());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Nota> create(@Valid @RequestBody Nota nota) {
        Nota saved = notaService.save(nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Nota> update(@PathVariable Long id, @Valid @RequestBody Nota nota) {
        if (!notaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        nota.setId(id);
        Nota updated = notaService.save(nota);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!notaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        notaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Nota>> findByEstudiante(@PathVariable Long estudianteId) {
        // Si es alumno, solo puede ver sus propias notas
        if (sessionManager.esAlumno() && !estudianteId.equals(sessionManager.getIdUsuarioActual())) {
            throw new RuntimeException("No tiene permisos para ver las notas de otro estudiante");
        }
        
        return ResponseEntity.ok(notaService.findByEstudianteId(estudianteId));
    }
    
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<Nota>> findByMateria(@PathVariable Long materiaId) {
        return ResponseEntity.ok(notaService.findByMateriaId(materiaId));
    }
    
    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<List<Nota>> findByEstudianteAndMateria(
            @PathVariable Long estudianteId, 
            @PathVariable Long materiaId) {
        
        // Si es alumno, solo puede ver sus propias notas
        if (sessionManager.esAlumno() && !estudianteId.equals(sessionManager.getIdUsuarioActual())) {
            throw new RuntimeException("No tiene permisos para ver las notas de otro estudiante");
        }
        
        return ResponseEntity.ok(notaService.findByEstudianteIdAndMateriaId(estudianteId, materiaId));
    }
    
    @GetMapping("/promedio/{estudianteId}")
    public ResponseEntity<Map<String, Double>> calcularPromedio(@PathVariable Long estudianteId) {
        // Si es alumno, solo puede ver su propio promedio
        if (sessionManager.esAlumno() && !estudianteId.equals(sessionManager.getIdUsuarioActual())) {
            throw new RuntimeException("No tiene permisos para ver el promedio de otro estudiante");
        }
        
        Double promedio = notaService.calcularPromedioEstudiante(estudianteId);
        Map<String, Double> response = new HashMap<>();
        response.put("promedio", promedio);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/acumulada/{estudianteId}/{materiaId}")
    public ResponseEntity<Map<String, Double>> calcularNotaAcumulada(
            @PathVariable Long estudianteId,
            @PathVariable Long materiaId) {
        
        // Si es alumno, solo puede ver su propia nota
        if (sessionManager.esAlumno() && !estudianteId.equals(sessionManager.getIdUsuarioActual())) {
            throw new RuntimeException("No tiene permisos para ver la nota de otro estudiante");
        }
        
        Double notaAcumulada = notaService.calcularNotaAcumulada(estudianteId, materiaId);
        Map<String, Double> response = new HashMap<>();
        response.put("notaAcumulada", notaAcumulada);
        return ResponseEntity.ok(response);
    }
}