package com.javeriana.gestionnotas.controller;

import com.javeriana.gestionnotas.model.Usuario;
import com.javeriana.gestionnotas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return usuarioService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Usuario> create(@Valid @RequestBody Usuario usuario) {
        Usuario saved = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario updated = usuarioService.save(usuario);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> findByRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.findByRol(rol));
    }
}