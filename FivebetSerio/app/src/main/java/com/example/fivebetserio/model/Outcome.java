package com.example.fivebetserio.model;


//classe usata per la decodifica dei dati ritornati dall'API per i match
public class Outcome {
    private String name;
    private String price;

    public Outcome(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
