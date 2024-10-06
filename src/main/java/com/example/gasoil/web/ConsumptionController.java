package com.example.gasoil.web;

import com.example.gasoil.dao.entities.Consumption;
import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.service.ConsumptionManagerService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class ConsumptionController {

    private static final Logger logger = LoggerFactory.getLogger(EngineController.class);

    private final ConsumptionManagerService consumptionManagerService;
    private final EngineManagerService engineManagerService;

    public ConsumptionController(ConsumptionManagerService consumptionManagerService, EngineManagerService engineManagerService){
        this.consumptionManagerService = consumptionManagerService;
        this.engineManagerService = engineManagerService;
    }

    @PostMapping(path="api/newconsumption")
    public ResponseEntity<Object> addConsumption(@RequestParam(name="consumptionDate")LocalDate consumptionDate, @RequestParam(name="consumption") double consumption, @RequestParam(name = "matricule") String matricule){
        logger.info("add a new consumption.");
        Engine engine = engineManagerService.getEngineByMatricule(matricule);
        Consumption newConsumption = new Consumption(consumptionDate,consumption,engine);
        consumptionManagerService.addConsumption(newConsumption);

        return new ResponseEntity<>(newConsumption,HttpStatus.OK);

    }

    // Return all the consumptions of a specific engine by its matricule.
   @GetMapping(path="api/consumptions")
   public ResponseEntity<Object> getAllConsumptionsByEngine(@RequestParam(name = "matricule") String matricule){
       logger.info("return all consumptions of the engine: {}", matricule);
        List<Consumption> consumptionsByMatricule = consumptionManagerService.getAllConsumptionsByEngine(matricule);
        return new ResponseEntity<>(consumptionsByMatricule, HttpStatus.OK);
   }

   @GetMapping(path="api/totalconsumption")
   public ResponseEntity<Object> calculateTotalconsumption(@RequestParam(name="matricule") String matricule, LocalDate startDate, LocalDate endDate){
        logger.info("calculate Total Consumption for the matricule: {} ", matricule);
        double totalConsumption = consumptionManagerService.calculateTotalConsumptionBetweenTwoDates(matricule,startDate,endDate);
        return new ResponseEntity<>(totalConsumption,HttpStatus.OK);
   }


}
