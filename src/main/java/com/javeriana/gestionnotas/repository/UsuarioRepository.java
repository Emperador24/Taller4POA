package com.javeriana.gestionnotas.repository;

import com.javeriana.gestionnotas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByEmailAndContrasena(String email, String contrasena);
    
    List<Usuario> findByRol(String rol);
    
    Optional<Usuario> findByCodigoEstudiante(String codigoEstudiante);
    
    boolean existsByEmail(String email);
}