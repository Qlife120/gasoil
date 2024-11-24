package com.example.gasoil.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer consumptionId;
    private LocalDate consumptionDate;
    @NotNull
    private double consumption;

    // Each consumption is related to a specific engine.
    @ManyToOne
    @JoinColumn(name = "engine_id") // This ensures Hibernate maps to the correct column
    private Engine engine;

    public Consumption(LocalDate consumptionDate, double consumptionValue, Engine engine){
        this.consumptionDate=consumptionDate;
        this.consumption=consumptionValue;

        this.engine=engine;

    }


}
