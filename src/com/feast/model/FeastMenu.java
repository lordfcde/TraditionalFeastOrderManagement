package com.feast.model;

// Lớp FeastMenu đại diện cho thông tin thực đơn tiệc
public class FeastMenu {
    private String menuCode;
    private String name;
    private double price;
    private String ingredients;

    public FeastMenu(String menuCode, String name, double price, String ingredients) {
        this.menuCode = menuCode;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    // Các phương thức Getter
    public String getMenuCode() {
        return menuCode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getIngredients() {
        return ingredients;
    }

    // Các phương thức Setter
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}