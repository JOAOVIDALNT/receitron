package com.joaovidal.receitron.domain.model;

public class Recipe {
    private int Id;
    private String title;
    private String category;
    private String culture;
    private String instructions;

    public Recipe(int id, String title, String category, String culture, String instructions) {
        Id = id;
        this.title = title;
        this.category = category;
        this.culture = culture;
        this.instructions = instructions;
    }

    public Recipe() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
