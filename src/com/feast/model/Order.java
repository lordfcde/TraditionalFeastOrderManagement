package com.feast.model;

import java.io.Serializable;
import java.util.Date;

// Lớp Order đại diện cho một đơn đặt tiệc
public class Order implements Serializable {
    private int orderId;
    private String customerCode;
    private String menuCode;
    private Date eventDate;
    private int numberOfTables;
    private double totalCost;

    public Order(int orderId, String customerCode, String menuCode, Date eventDate, int numberOfTables, double totalCost) {
        this.orderId = orderId;
        this.customerCode = customerCode;
        this.menuCode = menuCode;
        this.eventDate = eventDate;
        this.numberOfTables = numberOfTables;
        this.totalCost = totalCost;
    }

    // Các phương thức Getter và Setter
    public int getOrderId() {
        return orderId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getMenuCode() {
        return menuCode;
    }
    
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}