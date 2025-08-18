package com.feast.manager;

import com.feast.model.Customer;
import com.feast.model.FeastMenu;
import com.feast.model.Order;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Lớp quản lý chính chứa toàn bộ logic nghiệp vụ
public class OrderManagement {
    // Danh sách lưu trữ khách hàng và đơn hàng
    private List<Customer> customerList;
    private List<Order> orderList;
    private List<FeastMenu> menuList;
    private Scanner scanner;

    public OrderManagement() {
        this.customerList = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.menuList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadFeastMenus(); // Tải thực đơn khi khởi tạo
        // Tải dữ liệu từ file nếu có
        loadCustomersFromFile();
        loadOrdersFromFile();
    }

    // === CHỨC NĂNG 1: Đăng ký Khách hàng ===
    public void registerCustomer() {
        System.out.println("--- ĐĂNG KÝ KHÁCH HÀNG ---");
        String customerCode;
        String name;
        String phoneNumber;
        String email;

        do {
            // Nhập mã khách hàng và kiểm tra tính duy nhất, hợp lệ
            do {
                System.out.print("Nhập mã khách hàng (ví dụ C1234): ");
                customerCode = scanner.nextLine().trim();
            } while (!isValidCustomerCode(customerCode) || isCustomerCodeDuplicated(customerCode));

            // Nhập các thông tin khác và kiểm tra hợp lệ
            do {
                System.out.print("Nhập tên (2-25 ký tự): ");
                name = scanner.nextLine().trim();
            } while (!isValidName(name));

            do {
                System.out.print("Nhập số điện thoại (10 số, đầu số Việt Nam): ");
                phoneNumber = scanner.nextLine().trim();
            } while (!isValidPhoneNumber(phoneNumber));

            do {
                System.out.print("Nhập email (đúng định dạng): ");
                email = scanner.nextLine().trim();
            } while (!isValidEmail(email));

            // Tạo đối tượng khách hàng mới và thêm vào danh sách
            Customer newCustomer = new Customer(customerCode, name, phoneNumber, email);
            customerList.add(newCustomer);
            System.out.println("Đăng ký khách hàng thành công!");

            System.out.print("Bạn có muốn tiếp tục đăng ký khách hàng khác không? (Y/N): ");
        } while (scanner.nextLine().trim().equalsIgnoreCase("Y"));
    }

    // Các phương thức kiểm tra hợp lệ
    private boolean isValidCustomerCode(String code) {
        // Kiểm tra mã khách hàng bắt đầu bằng C, G, K và 4 chữ số
        return code.matches("[CGK]\\d{4}");
    }

    private boolean isCustomerCodeDuplicated(String code) {
        // Kiểm tra tính duy nhất của mã
        for (Customer c : customerList) {
            if (c.getCustomerCode().equalsIgnoreCase(code)) {
                System.out.println("Mã khách hàng đã tồn tại. Vui lòng nhập mã khác.");
                return true;
            }
        }
        return false;
    }
    
    private boolean isValidName(String name) {
        if (name.isEmpty() || name.length() < 2 || name.length() > 25) {
            System.out.println("Tên không được rỗng và phải từ 2-25 ký tự.");
            return false;
        }
        return true;
    }
    
    private boolean isValidPhoneNumber(String phone) {
        // Kiểm tra 10 chữ số và đầu số Việt Nam
        if (!phone.matches("\\d{10}")) {
            System.out.println("Số điện thoại phải có đúng 10 chữ số.");
            return false;
        }
        // Thêm logic kiểm tra đầu số nhà mạng
        String[] prefixes = {"090", "091", "092", "093", "094", "096", "097", "098", "099", "086", "088", "089", "070", "079", "077", "076", "078", "032", "033", "034", "035", "036", "037", "038", "039"};
        for (String prefix : prefixes) {
            if (phone.startsWith(prefix)) {
                return true;
            }
        }
        System.out.println("Số điện thoại không thuộc nhà mạng Việt Nam.");
        return false;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Email không hợp lệ.");
            return false;
        }
        return true;
    }


    // === CHỨC NĂNG 2: Cập nhật Thông tin Khách hàng ===
    public void updateCustomer() {
        System.out.println("--- CẬP NHẬT THÔNG TIN KHÁCH HÀNG ---");
        System.out.print("Nhập mã khách hàng cần cập nhật: ");
        String customerCode = scanner.nextLine().trim();

        Customer customerToUpdate = findCustomerByCode(customerCode);

        if (customerToUpdate == null) {
            System.out.println("This customer does not exist."); // Thông báo khi không tìm thấy
            return;
        }

        // Cập nhật tên
        System.out.print("Nhập tên mới (nhấn Enter để bỏ qua): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            if (isValidName(newName)) {
                customerToUpdate.setCustomerName(newName);
            }
        }

        // Cập nhật số điện thoại
        System.out.print("Nhập số điện thoại mới (nhấn Enter để bỏ qua): ");
        String newPhone = scanner.nextLine().trim();
        if (!newPhone.isEmpty()) {
            if (isValidPhoneNumber(newPhone)) {
                customerToUpdate.setPhoneNumber(newPhone);
            }
        }

        // Cập nhật email
        System.out.print("Nhập email mới (nhấn Enter để bỏ qua): ");
        String newEmail = scanner.nextLine().trim();
        if (!newEmail.isEmpty()) {
            if (isValidEmail(newEmail)) {
                customerToUpdate.setEmail(newEmail);
            }
        }

        System.out.println("Cập nhật thông tin khách hàng thành công!");
    }

    private Customer findCustomerByCode(String code) {
        for (Customer c : customerList) {
            if (c.getCustomerCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }


    // === CHỨC NĂNG 3: Tìm kiếm Khách hàng theo Tên ===
    public void searchCustomerByName() {
        System.out.println("--- TÌM KIẾM KHÁCH HÀNG THEO TÊN ---");
        System.out.print("Nhập tên (hoặc một phần tên) khách hàng: ");
        String nameSearch = scanner.nextLine().trim();

        List<Customer> matchingCustomers = new ArrayList<>();
        for (Customer c : customerList) {
            if (c.getCustomerName().toLowerCase().contains(nameSearch.toLowerCase())) {
                matchingCustomers.add(c);
            }
        }

        if (matchingCustomers.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            // Sắp xếp theo tên theo thứ tự bảng chữ cái
            Collections.sort(matchingCustomers, Comparator.comparing(Customer::getCustomerName));
            System.out.println("Matching Customers: " + nameSearch);
            System.out.printf("%-10s | %-25s | %-15s | %-30s%n", "Code", "Customer Name", "Phone", "Email");
            System.out.println("--------------------------------------------------------------------------------");
            for (Customer c : matchingCustomers) {
                System.out.printf("%-10s | %-25s | %-15s | %-30s%n", c.getCustomerCode(), c.getCustomerName(), c.getPhoneNumber(), c.getEmail());
            }
        }
    }


    // === CHỨC NĂNG 4: Hiển thị Thực đơn Tiệc ===
    public void displayFeastMenus() {
        System.out.println("--- DANH SÁCH THỰC ĐƠN TIỆC ---");
        if (menuList.isEmpty()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }

        // Sắp xếp theo giá tăng dần
        Collections.sort(menuList, Comparator.comparingDouble(FeastMenu::getPrice));
        
        System.out.println("List of Set Menus for ordering party:");
        for (FeastMenu menu : menuList) {
            System.out.println("Code         : " + menu.getMenuCode());
            System.out.println("Name         : " + menu.getName());
            // Sử dụng DecimalFormat để định dạng giá tiền
            java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
            System.out.println("Price        : " + formatter.format(menu.getPrice()) + " Vnd");
            System.out.println("Ingredients  : " + menu.getIngredients().replace(";", "\n             + "));
            System.out.println("-----------------------------------------------------------------");
        }
    }
    
    private void loadFeastMenus() {
        // Đọc dữ liệu từ file CSV
        String csvFile = "data/feastMenu.csv";
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length == 4) {
                    try {
                        String menuCode = data[0];
                        String name = data[1];
                        double price = Double.parseDouble(data[2]);
                        String ingredients = data[3];
                        menuList.add(new FeastMenu(menuCode, name, price, ingredients));
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi định dạng giá tiền ở dòng: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
        }
    }
    

    // === CHỨC NĂNG 5: Đặt Đơn hàng ===
    public void placeOrder() {
        System.out.println("--- ĐẶT TIỆC MỚI ---");
        String customerCode;
        String menuCode;
        int numberOfTables;
        Date eventDate;

        do {
            System.out.print("Nhập mã khách hàng đã đăng ký: ");
            customerCode = scanner.nextLine().trim();
            if (findCustomerByCode(customerCode) == null) {
                System.out.println("Mã khách hàng không tồn tại. Vui lòng thử lại.");
            }
        } while (findCustomerByCode(customerCode) == null);

        do {
            System.out.print("Nhập mã thực đơn tiệc: ");
            menuCode = scanner.nextLine().trim();
            if (findMenuByCode(menuCode) == null) {
                System.out.println("Mã thực đơn không tồn tại. Vui lòng thử lại.");
            }
        } while (findMenuByCode(menuCode) == null);

        do {
            System.out.print("Nhập số lượng bàn (> 0): ");
            try {
                numberOfTables = Integer.parseInt(scanner.nextLine().trim());
                if (numberOfTables <= 0) {
                    System.out.println("Số bàn phải lớn hơn 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
                numberOfTables = -1; // Đặt giá trị không hợp lệ
            }
        } while (numberOfTables <= 0);

        do {
            System.out.print("Nhập ngày tổ chức sự kiện (dd/MM/yyyy): ");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                eventDate = sdf.parse(scanner.nextLine().trim());
                if (eventDate.before(new Date())) {
                    System.out.println("Ngày sự kiện phải là ngày trong tương lai.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ngày không hợp lệ. Vui lòng nhập đúng định dạng dd/MM/yyyy.");
                eventDate = null;
            }
        } while (true);

        // Kiểm tra đơn hàng trùng lặp
        if (isOrderDuplicated(customerCode, menuCode, eventDate)) {
            System.out.println("Duplicate data !");
            return;
        }

        // Tạo order ID và tính toán tổng chi phí
        int orderId = generateOrderId();
        FeastMenu selectedMenu = findMenuByCode(menuCode);
        double totalCost = selectedMenu.getPrice() * numberOfTables;

        // Tạo đối tượng Order và thêm vào danh sách
        Order newOrder = new Order(orderId, customerCode, menuCode, eventDate, numberOfTables, totalCost);
        orderList.add(newOrder);

        System.out.println("Đặt tiệc thành công!");
        displayOrderInfo(newOrder);
    }
    
    private FeastMenu findMenuByCode(String code) {
        for (FeastMenu menu : menuList) {
            if (menu.getMenuCode().equalsIgnoreCase(code)) {
                return menu;
            }
        }
        return null;
    }
    
    private boolean isOrderDuplicated(String customerCode, String menuCode, Date eventDate) {
        for (Order order : orderList) {
            // So sánh combination 3 thuộc tính
            if (order.getCustomerCode().equalsIgnoreCase(customerCode) &&
                order.getMenuCode().equalsIgnoreCase(menuCode) &&
                order.getEventDate().equals(eventDate)) {
                return true;
            }
        }
        return false;
    }

    private int generateOrderId() {
        // Tạo một ID duy nhất
        if (orderList.isEmpty()) {
            return 1;
        }
        return orderList.get(orderList.size() - 1).getOrderId() + 1;
    }

    private void displayOrderInfo(Order order) {
        System.out.println("Customer order information [Order ID: " + order.getOrderId() + "]");
        // Hiển thị chi tiết đơn hàng
        Customer customer = findCustomerByCode(order.getCustomerCode());
        FeastMenu menu = findMenuByCode(order.getMenuCode());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");

        System.out.println("Code                 : " + customer.getCustomerCode());
        System.out.println("Customer name        : " + customer.getCustomerName());
        System.out.println("Phone number         : " + customer.getPhoneNumber());
        System.out.println("Email                : " + customer.getEmail());
        System.out.println("Code of Set Menu     : " + menu.getMenuCode());
        System.out.println("Set menu name        : " + menu.getName());
        System.out.println("Event date           : " + sdf.format(order.getEventDate()));
        System.out.println("Number of tables     : " + order.getNumberOfTables());
        System.out.println("Price                : " + formatter.format(menu.getPrice()) + " Vnd");
        System.out.println("Ingredients          : " + menu.getIngredients().replace(";", "\n                     + "));
        System.out.println("Total cost           : " + formatter.format(order.getTotalCost()) + " Vnd");
    }


    // === CHỨC NĂNG 6: Cập nhật thông tin đơn hàng ===
    public void updateOrder() {
        System.out.println("--- CẬP NHẬT THÔNG TIN ĐƠN HÀNG ---");
        System.out.print("Nhập mã đơn hàng (Order ID) cần cập nhật: ");
        int orderId;
        try {
            orderId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Mã đơn hàng không hợp lệ.");
            return;
        }

        Order orderToUpdate = findOrderByID(orderId);
        if (orderToUpdate == null) {
            System.out.println("This Order does not exist.");
            return;
        }
        
        // Kiểm tra ngày sự kiện đã qua chưa
        if (orderToUpdate.getEventDate().before(new Date())) {
            System.out.println("Không thể cập nhật đơn hàng có ngày sự kiện đã qua.");
            return;
        }
        
        boolean updated = false;

        // Cập nhật mã thực đơn
        System.out.print("Nhập mã thực đơn mới (nhấn Enter để bỏ qua): ");
        String newMenuCode = scanner.nextLine().trim();
        if (!newMenuCode.isEmpty()) {
            if (findMenuByCode(newMenuCode) != null) {
                orderToUpdate.setMenuCode(newMenuCode);
                updated = true;
            } else {
                System.out.println("Mã thực đơn không hợp lệ.");
            }
        }
        
        // Cập nhật số bàn
        System.out.print("Nhập số bàn mới (> 0, nhấn Enter để bỏ qua): ");
        String newTablesStr = scanner.nextLine().trim();
        if (!newTablesStr.isEmpty()) {
            try {
                int newTables = Integer.parseInt(newTablesStr);
                if (newTables > 0) {
                    orderToUpdate.setNumberOfTables(newTables);
                    updated = true;
                } else {
                    System.out.println("Số bàn phải lớn hơn 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
        
        // Cập nhật ngày sự kiện
        System.out.print("Nhập ngày sự kiện mới (dd/MM/yyyy, nhấn Enter để bỏ qua): ");
        String newDateStr = scanner.nextLine().trim();
        if (!newDateStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                Date newDate = sdf.parse(newDateStr);
                if (newDate.after(new Date())) {
                    orderToUpdate.setEventDate(newDate);
                    updated = true;
                } else {
                    System.out.println("Ngày sự kiện phải là ngày trong tương lai.");
                }
            } catch (Exception e) {
                System.out.println("Ngày không hợp lệ.");
            }
        }

        if (updated) {
            // Cập nhật lại total cost nếu có thay đổi
            FeastMenu menu = findMenuByCode(orderToUpdate.getMenuCode());
            orderToUpdate.setTotalCost(menu.getPrice() * orderToUpdate.getNumberOfTables());
            System.out.println("Cập nhật thông tin đơn hàng thành công!");
        } else {
            System.out.println("Không có thông tin nào được cập nhật.");
        }
    }
    
    private Order findOrderByID(int orderId) {
        for (Order order : orderList) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }


    // === CHỨC NĂNG 7: Lưu dữ liệu vào file ===
    public void saveToFile() {
        try {
            // Lưu danh sách khách hàng
            FileOutputStream fosCustomers = new FileOutputStream("data/customers.dat");
            ObjectOutputStream oosCustomers = new ObjectOutputStream(fosCustomers);
            oosCustomers.writeObject(customerList);
            oosCustomers.close();
            fosCustomers.close();
            System.out.println("Customer data has been successfully saved to \"customers.dat\".");

            // Lưu danh sách đơn hàng
            FileOutputStream fosOrders = new FileOutputStream("data/feast_order_service.dat");
            ObjectOutputStream oosOrders = new ObjectOutputStream(fosOrders);
            oosOrders.writeObject(orderList);
            oosOrders.close();
            fosOrders.close();
            System.out.println("Order data has been successfully saved to \"feast_order_service.dat\".");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu vào file: " + e.getMessage());
        }
    }

    private void loadCustomersFromFile() {
        try {
            FileInputStream fis = new FileInputStream("data/customers.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            customerList = (ArrayList<Customer>) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Tải dữ liệu khách hàng từ file thành công.");
        } catch (FileNotFoundException e) {
            System.out.println("File customers.dat không tồn tại. Bắt đầu với danh sách trống.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tải dữ liệu khách hàng: " + e.getMessage());
        }
    }

    private void loadOrdersFromFile() {
        try {
            FileInputStream fis = new FileInputStream("data/feast_order_service.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            orderList = (ArrayList<Order>) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Tải dữ liệu đơn hàng từ file thành công.");
        } catch (FileNotFoundException e) {
            System.out.println("File feast_order_service.dat không tồn tại. Bắt đầu với danh sách trống.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tải dữ liệu đơn hàng: " + e.getMessage());
        }
    }


    // === CHỨC NĂNG 8: Hiển thị danh sách Khách hàng hoặc Đơn hàng ===
    public void displayLists() {
        System.out.println("--- HIỂN THỊ DANH SÁCH ---");
        System.out.println("1. Hiển thị danh sách Khách hàng");
        System.out.println("2. Hiển thị danh sách Đơn hàng");
        System.out.print("Chọn chức năng (1 hoặc 2): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                displayCustomerList();
                break;
            case "2":
                displayOrderList();
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                break;
        }
    }

    private void displayCustomerList() {
        if (customerList.isEmpty()) {
            System.out.println("Does not have any customer information.");
            return;
        }
        // Sắp xếp theo tên khách hàng theo thứ tự bảng chữ cái
        Collections.sort(customerList, Comparator.comparing(Customer::getCustomerName));
        System.out.println("Customers information:");
        System.out.printf("%-10s | %-25s | %-15s | %-30s%n", "Code", "Customer Name", "Phone", "Email");
        System.out.println("--------------------------------------------------------------------------------");
        for (Customer c : customerList) {
            System.out.printf("%-10s | %-25s | %-15s | %-30s%n", c.getCustomerCode(), c.getCustomerName(), c.getPhoneNumber(), c.getEmail());
        }
    }

    private void displayOrderList() {
        if (orderList.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }
        // Sắp xếp theo ngày sự kiện tăng dần
        Collections.sort(orderList, Comparator.comparing(Order::getEventDate));
        System.out.println("Orders information:");
        System.out.printf("%-5s | %-15s | %-15s | %-10s | %-10s | %-15s%n", "ID", "Event date", "Customer ID", "Set Menu", "Tables", "Cost");
        System.out.println("--------------------------------------------------------------------------------------");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        for (Order o : orderList) {
            System.out.printf("%-5d | %-15s | %-15s | %-10s | %-10d | %-15s%n", o.getOrderId(), sdf.format(o.getEventDate()), o.getCustomerCode(), o.getMenuCode(), o.getNumberOfTables(), formatter.format(o.getTotalCost()));
        }
    }
}