package com.example.ecommerce;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import java.sql.ResultSet;

public class Product {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleStringProperty imageLocation;
    private SimpleIntegerProperty quantity;

    public void setId(int id) {
        this.id.set(id);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getId(){
        return id.get();
    }
    public String getName(){
        return name.get();
    }
    public Double getPrice(){
        return price.get();
    }

    public String getImageLocation() {
        return imageLocation.get();
    }

    public int getQuantity(){
        return quantity.get();
    }


    public Product(int id, String name, Double price, String imageLocation, int quantity){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.imageLocation = new SimpleStringProperty(imageLocation);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public  Product(){
        this.id = new SimpleIntegerProperty(0);
        this.quantity = new SimpleIntegerProperty(0);
    }

    public static ObservableList<Product> getAllProducts(String searchText, int page){
        String searchQuery = "select * from products where name like '%" + searchText +"%' limit " + String.valueOf((page-1)*6) + ", 6";
        ObservableList<Product> list = getProducts(searchQuery);
        ECommerce.getPage(list.size());
        return list;
    }

    public static ObservableList<Product> getProducts(String query){
        DatabaseConnection dbConn = new DatabaseConnection();
        ResultSet rs = dbConn.getQueryTable(query);
        ObservableList<Product> result = FXCollections.observableArrayList();
        try{
            if (rs != null) {
                while (rs.next()){
                    //taking out values from resultSet
                    result.add(new Product(rs.getInt("pid"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("imageLocation"),
                            rs.getInt("quantity")
                            )
                    );
                }
            }
        }

        catch (Exception e){
            e.printStackTrace();;
        }
        return result;
    }
}
