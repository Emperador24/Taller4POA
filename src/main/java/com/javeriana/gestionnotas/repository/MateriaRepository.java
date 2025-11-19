package com.javeriana.gestionnotas.repository;

import com.javeriana.gestionnotas.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    
    Optional<Materia> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}