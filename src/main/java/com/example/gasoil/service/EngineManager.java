package com.example.gasoil.service;

import com.example.gasoil.dao.entities.Engine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EngineManager {

    public Engine addEngine(Engine engine);
    public List<Engine> getAllEngines();
    public Engine getEngineByMatricule(String matricule);
    public List<Engine> searchEngine(String matricule);
}
