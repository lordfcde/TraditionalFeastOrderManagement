package com.feast.model;

import java.io.Serializable;

// Lớp Customer đại diện cho thông tin khách hàng
public class Customer implements Serializable {
    // Thuộc tính private để đảm bảo tính đóng gói
    private String customerCode;
    private String customerName;
    private String phoneNumber;
    private String email;

    // Constructor để khởi tạo đối tượng
    public Customer(String customerCode, String customerName, String phoneNumber, String email) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Các phương thức Getter và Setter công khai
    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Ghi đè phương thức toString() để hiển thị thông tin
    @Override
    public String toString() {
        return "Customer{" +
                "customerCode='" + customerCode + '\'' +
                ", name='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}