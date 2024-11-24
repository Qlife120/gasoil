package com.example.gasoil.web;

import com.example.gasoil.dao.entities.Consumption;
import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.service.ConsumptionManager;
import com.example.gasoil.service.ConsumptionManagerService;
import com.example.gasoil.service.EngineManagerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@RestController
@Slf4j
public class ConsumptionController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionController.class);

    private final ConsumptionManagerService consumptionManagerService;
    private final EngineManagerService engineManagerService;


    public ConsumptionController(ConsumptionManagerService consumptionManagerService, EngineManagerService engineManagerService){
        this.consumptionManagerService = consumptionManagerService;
        this.engineManagerService = engineManagerService;

    }

    // Tested
    @PostMapping(path="api/consumption/newconsumption")
    public ResponseEntity<Object> addConsumption(@RequestParam(name="consumptionDate")LocalDate consumptionDate, @RequestParam(name="consumption") double consumption, @RequestParam(name = "matricule") String matricule){
        logger.info("add a new consumption.");
        Engine engine = engineManagerService.getEngineByMatricule(matricule);
        Consumption newConsumption = new Consumption(consumptionDate,consumption,engine);
        consumptionManagerService.addConsumption(newConsumption);

        return new ResponseEntity<>(newConsumption,HttpStatus.OK);

    }
    @GetMapping(path="api/consumption/allconsumptions")
    public ResponseEntity<Object> getAllConsumptions(){
        logger.info("return all consumptions.");
        List<Consumption> consumptionsByMatricule = consumptionManagerService.getAll();
        return new ResponseEntity<>(consumptionsByMatricule, HttpStatus.OK);
    }
    // Return all the consumptions of a specific engine by its matricule.
    // Tested
   @GetMapping(path="api/consumption/consumptions")
   public ResponseEntity<Object> getAllConsumptionsByEngine(@RequestParam(name = "matricule") String matricule){
       logger.info("return all consumptions of the engine: {}", matricule);
        List<Consumption> consumptionsByMatricule = consumptionManagerService.getAllConsumptionsByEngine(matricule);
        return new ResponseEntity<>(consumptionsByMatricule, HttpStatus.OK);
   }

   // return the total consumption of an engine between two dates (startDate - endDate) included
    // Tested - bug fixed
   @GetMapping(path="api/consumption/totalconsumptiondates")
   public ResponseEntity<Object> calculateTotalconsumption(@RequestParam(name="matricule") String matricule, LocalDate startDate, LocalDate endDate){
        logger.info("calculate Total Consumption for the matricule: {} ", matricule);
        double totalConsumption = consumptionManagerService.calculateTotalConsumptionBetweenTwoDates(matricule,startDate,endDate);
        return new ResponseEntity<>(totalConsumption,HttpStatus.OK);
   }

   // return the list of consumptions of an engine between two dates (list of consumption objects) [cons1,cons2,...]
   // tested
   @GetMapping(path="api/consumption/graphconsumptions")
   public ResponseEntity<Object> getGraphConsumption(@RequestParam(name="matricule") String matricule, LocalDate startDate, LocalDate endDate){
        logger.info("return data for consumptions graphs by engine.");
        TreeMap<LocalDate,Double> consumptionsDataGraph =   consumptionManagerService.getConsumptionsDataGraph(matricule,startDate,endDate);
        return new ResponseEntity<>(consumptionsDataGraph,HttpStatus.OK);
    }

   // total Consumption of the current month
    // tested
   @GetMapping(path="api/consumption/totalconsumptionmonth")
   public ResponseEntity<Object> getTotalConsumptionCurrentMonth(){
        logger.info("return total Consumption of Current Month");
        double totalConsumptionCurrentMonth = consumptionManagerService.getTotalConsumptionCurrentMonth();
        return new ResponseEntity<>(totalConsumptionCurrentMonth,HttpStatus.OK);
   }

   // map the months with the corresponding numbers (input : 1->12)
   // tested
   @GetMapping(path="api/consumption/totalconsumptionbymonth")
   public ResponseEntity<Object> getTotalConsumptionByMonth(@RequestParam(name="month") int month){
        logger.info("return total consumption of an engine by month.");
        double totalConsumptionByMonth = consumptionManagerService.getTotalConsumptionByMonth(month);
        return new ResponseEntity<>(totalConsumptionByMonth, HttpStatus.OK);
   }

   // return a pair of engine name , and it's consumption (Max consumption of the current month)
   // tested
   @GetMapping(path="api/consumption/maxtotalconsumption")
   public ResponseEntity<Object> getMaxConsumptionOfMonth(){
        logger.info("return max total consumption of the current month.");
       Pair<String,Double> maxTotalConsumption = consumptionManagerService.getMaxTotalConsumptionCurrentMonth();

       return new ResponseEntity<>(maxTotalConsumption,HttpStatus.OK);
   }
    
   // last consumptions added to database
   // tested
   @GetMapping(path="api/consumption/lasttenenginesadded")
   public ResponseEntity<Object> getLastTenenginesadded(){
        logger.info("return last ten consumptions added to database.");
        List<Consumption> lastTenConsumptions = consumptionManagerService.getLastTenConsumptions();
        return new ResponseEntity<>(lastTenConsumptions,HttpStatus.OK);
   }








}
