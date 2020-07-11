package com.protienperdollar.redone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product implements Serializable {
    private int noteNewLines;
    private String name, notes;
    private float price, weight, proteinPer100g, proteinPerDollar, pricePerKilo;

    public Product(String name, String notes, float price, float weight, float proteinPer100g, float proteinPerDollar) {
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.weight = weight;
        this.proteinPer100g = proteinPer100g;
        this.proteinPerDollar = proteinPerDollar;
        this.pricePerKilo = calculatePPK(price, weight);

        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(notes);
        while (m.find()) {
            noteNewLines++;
        }
    }

    public Product(BufferedReader in) throws IOException {
        this.name = in.readLine();
        this.noteNewLines = Integer.valueOf(in.readLine());
        StringBuilder str = new StringBuilder();
        for (int i = 0; i <= noteNewLines; i++) {
            str.append(in.readLine());
        }
        this.notes = str.toString();
        this.price = Float.valueOf(in.readLine());
        this.weight = Float.valueOf(in.readLine());
        this.proteinPer100g = Float.valueOf(in.readLine());
        this.proteinPerDollar = Float.valueOf(in.readLine());
        this.pricePerKilo = Float.valueOf(in.readLine());
    }

    public static float calculatePPD(float price, float weight, float proteinPer100g) {
        // grams of product per dollar * protein per gram
        return ((1/price) * weight) * (proteinPer100g/100);
    }

    public static float calculatePPK(float price, float weight) {
        // price / weight in kilos
        return price / (weight/1000);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        proteinPerDollar = calculatePPD(price, weight, proteinPer100g);
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        proteinPerDollar = calculatePPD(price, weight, proteinPer100g);
    }

    public float getProteinPer100g() {
        return proteinPer100g;
    }

    public void setProteinPer100g(float proteinPer100g) {
        this.proteinPer100g = proteinPer100g;
        proteinPerDollar = calculatePPD(price, weight, proteinPer100g);
    }

    public float getProteinPerDollar() {
        return proteinPerDollar;
    }
    public float getPricePerKilo() { return pricePerKilo; }

    public String toString() {
        return name + "\n" + noteNewLines + "\n" + notes + "\n" + price + "\n" + weight + "\n" + proteinPer100g + "\n" + proteinPerDollar + "\n" + pricePerKilo + "\n";
    }
}
