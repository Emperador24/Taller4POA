package com.javeriana.gestionnotas.service;

import com.javeriana.gestionnotas.model.*;
import com.javeriana.gestionnotas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail()) && usuario.getId() == null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }
    
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    public Optional<Usuario> autenticar(String email, String contrasena) {
        return usuarioRepository.findByEmailAndContrasena(email, contrasena);
    }
    
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
}