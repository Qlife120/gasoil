package com.example.gasoil.dao.repositories;

import com.example.gasoil.dao.entities.Engine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface EngineRepository extends JpaRepository<Engine, Integer> {
   Engine findEngineByEngineId(Integer id);
   Engine findEngineByMatricule(String matricule);
   List<Engine> findEngineByMatriculeContainingIgnoreCase(String matricule);

}
