package com.example.gasoil.dao.entities;

import com.example.gasoil.enums.state;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer engineId;
   // matricule de vehicule alphanumerique ou Numerique ex: 45676-A-13 => 45676A13
    @NotEmpty
    @Pattern(regexp = "[A-Za-z0-9 ]*")
    private String matricule;
    private String engineName;
    // Every attribute below is optional
    private String description;
    // Etat du vehicule En marche, en panne , defectueux.
    @Enumerated(EnumType.STRING)
    private state engineState;

    @OneToMany(mappedBy = "engine")
    @JsonIgnore
    private List<Consumption> consumptionList;

    public Engine(String matricule, String engineName){
        this.matricule = matricule;
        this.engineName = engineName;
    }






}
