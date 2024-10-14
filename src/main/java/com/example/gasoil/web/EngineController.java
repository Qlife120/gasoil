package com.example.gasoil.web;

import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.service.EngineManagerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class EngineController {

    private static final Logger logger = LoggerFactory.getLogger(EngineController.class);

    private final EngineManagerService engineManagerService;

    public EngineController(EngineManagerService engineManagerService){
        this.engineManagerService = engineManagerService;
    }

    @PostMapping(path="/api/engine/newengine")
    public ResponseEntity<Object> addEngine(@RequestParam(name ="matricule") String matricule, @RequestParam(name= "engineName") String engineName) {
        logger.info("adding a new engine with matricule {}", matricule  );
        Engine newEngine = new Engine(matricule, engineName);
        engineManagerService.addEngine(newEngine);
        return new ResponseEntity<>(newEngine,HttpStatus.OK);

    }

    @GetMapping(path="/api/engine/engines")
    public ResponseEntity<Object> getAllEngines(){
        logger.info("return all engines of database."); 
        List<Engine> engines = engineManagerService.getAllEngines();
        return new ResponseEntity<>(engines, HttpStatus.OK);
    }

    @GetMapping(path="/api/engine/enginesearch")
    public ResponseEntity<Object> getEngineByMatricule(String matricule){
        logger.info("return the engine with the matricule {}", matricule);
        List<Engine> enginesByMatricule = engineManagerService.searchEngine(matricule);
        return new ResponseEntity<>(enginesByMatricule,HttpStatus.OK);
    }

    @GetMapping(path="/api/engine/totalengines")
    public  ResponseEntity<Object> getTotalEngines(){
        logger.info("return total of engines in database.");
        int totalEngines = engineManagerService.getTotalEngines();
        return new ResponseEntity<>(totalEngines,HttpStatus.OK);

    }
}
