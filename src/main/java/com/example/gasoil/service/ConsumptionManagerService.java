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
    // should be fixed

    @Override
    public List<Consumption> getConsumptionsDataGraph(String matricule, LocalDate startDate, LocalDate endDate) {
        List<Consumption> consumptions = getAllConsumptionsByEngine(matricule);
        Engine engine = engineRepository.findEngineByMatricule(matricule);
        if(engine==null){
            throw new IllegalArgumentException("engine with matricule " + matricule + "not found;");
        }

        List<Consumption> filteredConsumptions = new ArrayList<>();
        for (Consumption consumption : consumptions) {
            LocalDate consumptionDate = consumption.getConsumptionDate();

            if ((consumptionDate.isAfter(startDate) && consumptionDate.isBefore(endDate)) || (consumptionDate.isEqual(startDate)) || consumptionDate.isEqual(endDate)) {
                filteredConsumptions.add(consumption);

            }
        }
        filteredConsumptions.sort(Comparator.comparing(Consumption::getConsumptionDate));
        return filteredConsumptions;
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
    @Override
    public Pair<String, Double> getMaxTotalConsumptionCurrentMonth() {
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
}
