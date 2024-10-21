package com.example.gasoil.dao.repositories;

import com.example.gasoil.dao.entities.Consumption;
import com.example.gasoil.dao.entities.Engine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface ConsumptionRepository extends JpaRepository<Consumption, Integer> {

    public  Consumption findConsumptionByConsumptionId(Integer id);
    public List<Consumption> findAllByEngine(Engine engine);
    public Consumption findConsumptionsByConsumptionDateBetweenAndEngine(LocalDate startDate, LocalDate endDate, Engine engine);
    public List<Consumption> findAll();
}
