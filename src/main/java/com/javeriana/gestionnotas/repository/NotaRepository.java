package com.javeriana.gestionnotas.repository;

import com.javeriana.gestionnotas.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    
    List<Nota> findByEstudianteId(Long estudianteId);
    
    List<Nota> findByMateriaId(Long materiaId);
    
    List<Nota> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
    
    @Query("SELECT SUM(n.porcentaje) FROM Nota n WHERE n.estudiante.id = :estudianteId AND n.materia.id = :materiaId")
    Integer sumPorcentajesByEstudianteAndMateria(@Param("estudianteId") Long estudianteId, 
                                                   @Param("materiaId") Long materiaId);
    
    @Query("SELECT SUM(n.porcentaje) FROM Nota n WHERE n.estudiante.id = :estudianteId AND n.materia.id = :materiaId AND n.id != :notaId")
    Integer sumPorcentajesExceptCurrent(@Param("estudianteId") Long estudianteId, 
                                        @Param("materiaId") Long materiaId,
                                        @Param("notaId") Long notaId);
}