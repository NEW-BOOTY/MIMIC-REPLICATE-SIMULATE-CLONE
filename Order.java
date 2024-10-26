/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */

import java.util.List;
import java.util.regex.Pattern;

public class Order {
    private String orderId;
    private String userId;
    private String paymentMethod;
    private String shippingAddress;
    private String orderDate;
    private List<String> orderItems;

    public Order(String orderId, String userId, String paymentMethod, String shippingAddress, String orderDate, List<String> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        validate();
    }

    private void validate() {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        if (paymentMethod == null || !isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
        if (shippingAddress == null || shippingAddress.isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        if (orderDate == null || !isValidDate(orderDate)) {
            throw new IllegalArgumentException("Invalid order date format");
        }
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod.equals("Credit Card") || paymentMethod.equals("PayPal") || paymentMethod.equals("Bank Transfer");
    }

    private boolean isValidDate(String date) {
        // Basic date validation pattern, format: YYYY-MM-DD
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
        return Pattern.matches(datePattern, date);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<String> getOrderItems() {
        return orderItems;
    }

    public static void main(String[] args) {
        try {
            // Example of creating a valid order
            List<String> items = List.of("Item1", "Item2");
            Order order = new Order("12345", "user001", "Credit Card", "123 Main St, City, Country", "2024-08-06", items);
            System.out.println("Order created successfully: " + order.getOrderId());

            // Example of creating an invalid order
            Order invalidOrder = new Order("", "", "Cash", "", "20240806", items);
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
    }
}

/**
 * Copyright © 2024 Devin B. Royal. All rights reserved.
 */
