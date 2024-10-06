package com.example.gasoil.service;

import com.example.gasoil.dao.entities.Consumption;
import com.example.gasoil.dao.entities.Engine;
import com.example.gasoil.dao.repositories.ConsumptionRepository;
import com.example.gasoil.dao.repositories.EngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumptionManagerService implements ConsumptionManager{

    private final EngineRepository engineRepository;
    private final ConsumptionRepository consumptionRepository;

    @Autowired
    public ConsumptionManagerService(ConsumptionRepository consumptionRepository, EngineRepository engineRepository){
        this.consumptionRepository = consumptionRepository;
        this.engineRepository = engineRepository;
    }

    @Override
    public Consumption addConsumption(Consumption consumption) {
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

        double total = 0;
        int taille = consumptions.size();
        for (int i = 0;i<taille;++i){
            LocalDate consumptionDate = consumptions.get(i).getConsumptionDate();
            if((consumptionDate.isAfter(startDate) && consumptionDate.isBefore(endDate)) || (consumptionDate.isEqual(startDate)) || consumptionDate.isEqual(endDate)){
                total+=consumptions.get(i).getConsumption();

            }
        }
        return total;
    }


}
