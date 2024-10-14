package com.example.gasoil.service;


import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.dao.repositories.EngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineManagerService implements EngineManager{

    private final EngineRepository engineRepository;

    @Autowired
    public EngineManagerService(EngineRepository engineRepository){
        this.engineRepository = engineRepository;
    }

    @Override
    public Engine addEngine(Engine engine) {
        return engineRepository.save(engine);
    }

    @Override
    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }

    // return engine by Matricule (the exact maticule)
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


}
