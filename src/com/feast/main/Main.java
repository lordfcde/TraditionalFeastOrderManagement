package com.feast.main;

import com.feast.manager.OrderManagement;
import java.util.Scanner;

// Lớp chính để chạy chương trình
public class Main {
    public static void main(String[] args) {
        OrderManagement manager = new OrderManagement();
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("\n--- TRADITIONAL FEAST ORDER MANAGEMENT ---");
            System.out.println("1. Đăng ký khách hàng");
            System.out.println("2. Cập nhật thông tin khách hàng");
            System.out.println("3. Tìm kiếm khách hàng theo tên");
            System.out.println("4. Hiển thị thực đơn tiệc");
            System.out.println("5. Đặt tiệc");
            System.out.println("6. Cập nhật thông tin đơn hàng");
            System.out.println("7. Lưu dữ liệu vào file");
            System.out.println("8. Hiển thị danh sách khách hàng hoặc đơn hàng");
            System.out.println("9. Thoát");
            System.out.print("Chọn chức năng: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manager.registerCustomer();
                    break;
                case "2":
                    manager.updateCustomer();
                    break;
                case "3":
                    manager.searchCustomerByName();
                    break;
                case "4":
                    manager.displayFeastMenus();
                    break;
                case "5":
                    manager.placeOrder();
                    break;
                case "6":
                    manager.updateOrder();
                    break;
                case "7":
                    manager.saveToFile();
                    break;
                case "8":
                    manager.displayLists();
                    break;
                case "9":
                    System.out.println("Đã thoát chương trình. Tạm biệt!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (!choice.equals("9"));
        
        scanner.close();
    }
}