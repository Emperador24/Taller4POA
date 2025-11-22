package com.javeriana.gestionnotas.service;

import com.javeriana.gestionnotas.model.Materia;
import com.javeriana.gestionnotas.repository.MateriaRepository;
import com.javeriana.gestionnotas.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MateriaService {
    
    @Autowired
    private MateriaRepository materiaRepository;
    
    public List<Materia> findAll() {
        return materiaRepository.findAll();
    }
    
    public Optional<Materia> findById(Long id) {
        return materiaRepository.findById(id);
    }
    
    public Materia save(Materia materia) {
        if (materiaRepository.existsByNombre(materia.getNombre()) && materia.getId() == null) {
            throw new ValidationException("El nombre de la materia ya existe");
        }
        return materiaRepository.save(materia);
    }
    
    public void deleteById(Long id) {
        materiaRepository.deleteById(id);
    }
}