package com.example.gasoil.service;

import com.example.gasoil.dao.entities.Consumption;
import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.dao.repositories.ConsumptionRepository;
import com.example.gasoil.dao.repositories.EngineRepository;


import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ConsumptionManagerService implements ConsumptionManager{

    private final EngineRepository engineRepository;
    private final ConsumptionRepository consumptionRepository;



    public ConsumptionManagerService(ConsumptionRepository consumptionRepository, EngineRepository engineRepository){
        this.consumptionRepository = consumptionRepository;
        this.engineRepository = engineRepository;

    }

    @Override
    public Consumption addConsumption(Consumption consumption) {
        LocalDate today = LocalDate.now();
        if (consumption.getEngine()== null ) {
            throw new IllegalArgumentException("Engine matricule is not found in the database.");
        }
        if(consumption.getConsumptionDate().isAfter(today)){
            throw new IllegalArgumentException("Date in the future can't be added");
        }
        if(consumption.getConsumption()<=0){
            throw new IllegalArgumentException("consumption value is negative or null.");
        }
        return consumptionRepository.save(consumption);
    }

    @Override
    public Consumption getConsumptionById(Integer id) {
        return consumptionRepository.findConsumptionByConsumptionId(id);
    }

    @Override
    public List<Consumption> getAllConsumptionsByEngine(String matricule) {
        Engine engine = engineRepository.findEngineByMatricule(matricule);
        return consumptionRepository.findAllByEngine(engine);

    }

    // calculate The total between two dates startDate and endDate both included
    @Override
    public double calculateTotalConsumptionBetweenTwoDates(String matricule, LocalDate startDate, LocalDate endDate) {
        List<Consumption> consumptions = getAllConsumptionsByEngine(matricule);
        Engine engine = engineRepository.findEngineByMatricule(matricule);
        if(engine==null){
            throw new IllegalArgumentException("engine not found.");
        }
        double total = 0;
        for (Consumption consumption : consumptions) {
            LocalDate consumptionDate = consumption.getConsumptionDate();
            if ((consumptionDate.isAfter(startDate) && consumptionDate.isBefore(endDate)) || (consumptionDate.isEqual(startDate)) || consumptionDate.isEqual(endDate)) {
                total += consumption.getConsumption();

            }
        }
        return total;
    }

    // Data Set of the consumption of an engine between two date -> Graph
    // bug fixed: returns a treeMap of dates and totalConsumption of date within the range startDate and endDate
    // the worst function ever made
    public TreeMap<LocalDate, Double> getConsumptionsDataGraph(String matricule, LocalDate startDate, LocalDate endDate) {

        List<Consumption> consumptions = getAllConsumptionsByEngine(matricule);
        // check if the engine is not null, not necessary in the new version 
        Engine engine = engineRepository.findEngineByMatricule(matricule);
        if(engine==null){
            throw new IllegalArgumentException("engine with matricule " + matricule + "not found;");
        }

        // list with all consumptions with dateConsumption between startDate and endDate.
        List<Consumption> filteredConsumptions = new ArrayList<>();

        // can be done without the for loop 

        for (Consumption consumption : consumptions) {
            LocalDate consumptionDate = consumption.getConsumptionDate();

            if ((consumptionDate.isAfter(startDate) && consumptionDate.isBefore(endDate)) || (consumptionDate.isEqual(startDate)) || consumptionDate.isEqual(endDate)) {
                filteredConsumptions.add(consumption);

            }
        }
        // Map with key: dateConsumption and value the total consumption of the date.

        Map<LocalDate, Double> filteredConsumptionMap = new HashMap<>();

        for (Consumption consumption: filteredConsumptions){
            LocalDate consumptionDate = consumption.getConsumptionDate();
            double consumptionValue = consumption.getConsumption();

            // merge: if the key already exists, we sum values. (Total of consumption) 

            filteredConsumptionMap.merge(consumptionDate,consumptionValue, Double:: sum);
        }
        TreeMap<LocalDate, Double> filteredConsumptionsTreeMap = new TreeMap<>();
        filteredConsumptionsTreeMap.putAll(filteredConsumptionMap);

        return filteredConsumptionsTreeMap;
    }

    // Month = currentMonth for dashboard case
    @Override
    public double getTotalConsumptionByMonth(int month) {
       List<Consumption> consumptions =  consumptionRepository.findAll();
       double total = 0;
       for(Consumption consumption: consumptions){
           if(consumption.getConsumptionDate().getMonthValue() == month){
               total+= consumption.getConsumption();
           }
       }
       return total;

    }

    // Total consumption of the current month
    @Override
    public double getTotalConsumptionCurrentMonth() {
        int currentMonth = LocalDate.now().getMonthValue();
        return  getTotalConsumptionByMonth(currentMonth);
    }

    // return a Pair , first contains the matricule second the max consumption of the engine in the current month
    // find other way

    // last 30 days

    @Override
    public Pair<String, Double> getMaxTotalConsumptionCurrentMonth() {

        // bug: we return all consumptions (Solution) => return only consumptions of the month
        List<Consumption> consumptionsCurrentMonth = consumptionRepository.findAll();

        Map<Integer,Double> consumptionsByEngineMap = new HashMap<>();
        
        int currentMonth = LocalDate.now().getMonthValue();
        for(Consumption consumption : consumptionsCurrentMonth){
            if(consumption.getConsumptionDate().getMonthValue()== currentMonth){
                Integer engineId = consumption.getEngine().getEngineId();
                double consumptionValue = consumption.getConsumption();
                consumptionsByEngineMap.merge(engineId,consumptionValue,Double::sum);
            }
        }

        Optional<Map.Entry<Integer,Double>> maxEntry =  consumptionsByEngineMap.entrySet().stream().max(Map.Entry.comparingByValue());

        if(maxEntry.isPresent()){
            Integer engineId = maxEntry.get().getKey();
            double maxConsumption = maxEntry.get().getValue();

            String matricule = engineRepository.findEngineByEngineId(engineId).getMatricule();

            return Pair.of(matricule,maxConsumption);
        }

        return Pair.of("0000",0.0);
    }

    @Override
    public List<Consumption> getLastTenConsumptions() {
        return consumptionRepository.findlastConsumptionAdded();
    }

    @Override
    public List<Consumption> getAll(){
        return consumptionRepository.findAll();
    }

}
