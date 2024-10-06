package com.example.gasoil.service;

import com.example.gasoil.dao.entities.Consumption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface ConsumptionManager {

    public Consumption addConsumption(Consumption consumption);
    public Consumption getConsumptionById(Integer id);
    public List<Consumption> getAllConsumptionsByEngine(String matricule);
    public double calculateTotalConsumptionBetweenTwoDates(String matricule, LocalDate startDate, LocalDate endDate);



}
