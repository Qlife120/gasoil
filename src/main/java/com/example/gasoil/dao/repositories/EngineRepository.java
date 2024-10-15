package com.example.gasoil.dao.repositories;

import com.example.gasoil.dao.entities.Engine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Transactional
public interface EngineRepository extends JpaRepository<Engine, Integer> {
   Engine findEngineByEngineId(Integer id);
   Engine findEngineByMatricule(String matricule);
   List<Engine> findEngineByMatriculeContainingIgnoreCase(String matricule);
   @Query(value="SELECT  * FROM  engine ORDER BY engine_id DESC LIMIT 10", nativeQuery = true)
   List<Engine> findlastEnginesAdded();
   Optional<Engine> findByMatricule(String matricule);
}
