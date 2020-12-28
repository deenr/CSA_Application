package be.kuleuven.csa.controller;

public class BestaandeBoerController {

    public String boerNaam;




    public void getNaamVanBestaandeBoer(String naam) {
        this.boerNaam = naam;
        System.out.println(boerNaam);
    }
}
