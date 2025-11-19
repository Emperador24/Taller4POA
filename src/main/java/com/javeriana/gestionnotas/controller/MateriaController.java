package com.javeriana.gestionnotas.controller;

import com.javeriana.gestionnotas.model.Materia;
import com.javeriana.gestionnotas.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    
    @Autowired
    private MateriaService materiaService;
    
    @GetMapping
    public ResponseEntity<List<Materia>> findAll() {
        return ResponseEntity.ok(materiaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Materia> findById(@PathVariable Long id) {
        return materiaService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Materia> create(@Valid @RequestBody Materia materia) {
        Materia saved = materiaService.save(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Materia> update(@PathVariable Long id, @Valid @RequestBody Materia materia) {
        if (!materiaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        materia.setId(id);
        Materia updated = materiaService.save(materia);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!materiaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        materiaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}