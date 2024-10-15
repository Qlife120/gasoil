package com.example.gasoil.service;


import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.dao.repositories.EngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EngineManagerService implements EngineManager{

    private final EngineRepository engineRepository;

    @Autowired
    public EngineManagerService(EngineRepository engineRepository){
        this.engineRepository = engineRepository;
    }

    @Override
    public Engine addEngine(Engine engine) {
        Optional<Engine> existingEngine = engineRepository.findByMatricule(engine.getMatricule());
        if(existingEngine.isPresent()){
            throw new IllegalArgumentException("An engine with this matricuLe exists already in database.");
        }
        return  engineRepository.save(engine);
    }

    @Override
    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }

    // return engine by Matricule (the exact matricule)
    @Override
    public Engine getEngineByMatricule(String matricule) {
        return engineRepository.findEngineByMatricule(matricule);
    }

    // return engine containing matricule ignoring cases
    @Override
    public List<Engine> searchEngine(String matricule) {
        return engineRepository.findEngineByMatriculeContainingIgnoreCase(matricule);
    }


    // return the number of engines in database
    @Override
    public int getTotalEngines() {
        int taille = engineRepository.findAll().size();
        return taille;
    }
    // return the last 10 engines added to database
    @Override
    public List<Engine> getLastTenEnginesAdded() {
        return engineRepository.findlastEnginesAdded();
    }




}
