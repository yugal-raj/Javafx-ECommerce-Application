package com.example.ecommerce;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Order {
    private final SimpleStringProperty productName;
    private final SimpleStringProperty productImageLocation;
    private final SimpleStringProperty dateTime;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;

    public Order(String productName, String imageLocation, String dateTime, int quantity, double price){
        this.productName = new SimpleStringProperty(productName);
        this.productImageLocation = new SimpleStringProperty(imageLocation);
        this.dateTime = new SimpleStringProperty(dateTime);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public String getProductName() {
        return productName.get();
    }


    public String getProductImageLocation() {
        return productImageLocation.get();
    }


    public int getQuantity() {
        return quantity.get();
    }

    public double getPrice() { return price.get(); }


    public static ObservableList<Order> getAllOrders(Customer customer){
        String searchQuery = "select orders.oid, products.name, products.imageLocation, orders.date_time, orders.quantity, products.price from orders inner join products on orders.product_id = products.pid where orders.customer_id = " + customer.getId() + " order by orders.date_time desc";
        ObservableList<Order> list = getOrders(searchQuery);
        ECommerce.getPage(list.size());
        return list;
    }

    public static ObservableList<Order> getOrders(String query){
        DatabaseConnection dbConn = new DatabaseConnection();
        ResultSet rs = dbConn.getQueryTable(query);
        ObservableList<Order> result = FXCollections.observableArrayList();
        try{
            if (rs != null) {
                while (rs.next()){
                    //taking out values from resultSet
                    result.add(new Order(
                                    rs.getString("name"),
                                    rs.getString("imageLocation"),
                                    rs.getString("date_time"),
                                    rs.getInt("quantity"),
                                    rs.getDouble("price")
                            )
                    );
                }
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean placeOrder(Customer customer, Product product, int quantity, String date_time){
        try{
            String placeOrder = "INSERT INTO orders(customer_id, product_id, quantity, status, date_time) VALUES(" + customer.getId() + "," + product.getId() + "," + quantity + ", 'Ordered','" + date_time +"');";
            String updateQuantity = "UPDATE `ecomm`.`products` SET `quantity` = '" + (product.getQuantity() - quantity) + "' WHERE (`pid` = '" + product.getId() + "');";
            DatabaseConnection dbConn = new DatabaseConnection();
            return (dbConn.insertUpdateCreate(placeOrder) & dbConn.insertUpdateCreate(updateQuantity));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
