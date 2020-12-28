package be.kuleuven.csa.controller;

import be.kuleuven.csa.domain.AuteurRepository;
import be.kuleuven.csa.domain.KlantRepository;
import be.kuleuven.csa.domain.PakketRepository;
import be.kuleuven.csa.domain.VerkooptRepository;
import javafx.scene.control.Button;

public class BestaandeBoerController {

    public String boerNaam;
    public Button nieuweWeekPakkettenToevoegenBoer_button;
    public Button productToevoegenBoer_button;
    public Button klantenBekijkenBoer_button;
    public Button pakketPrijsWijzingenBoer_button;

    private static AuteurRepository auteurRepository;
    private static KlantRepository klantRepository;
    private static PakketRepository pakketRepository;
    private static VerkooptRepository verkooptRepository;


    public void getNaamVanBestaandeBoer(String naam) {
        this.boerNaam = naam;
        System.out.println(boerNaam);
    }
}
