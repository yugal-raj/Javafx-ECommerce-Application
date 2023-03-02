package com.example.ecommerce;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Cart {

    private SimpleIntegerProperty cartId;
    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty quantity;

    private SimpleIntegerProperty productQuantity;

    public int getProductQuantity() {
        return productQuantity.get();
    }

    public int getCartId() {
        return cartId.get();
    }

    public int getProductId() {
        return productId.get();
    }


    public String getProductName() {
        return productName.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public Cart(int cartId, int productId, String productName, Double price, int quantity, int productQuantity){
        this.cartId = new SimpleIntegerProperty(cartId);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.productQuantity =new SimpleIntegerProperty(productQuantity) ;
    }

    public static ObservableList<Cart> getAllCart(Customer customer){

        //select cart3.cid, products.name, products.price, products.quantity as pQuantity, cart3.quantity from products inner join cart3 on products.pid = cart3.productId;
        String searchQuery = "select cart"+ String.valueOf(customer.getId()) +".cid, products.pid, products.name, products.price, products.quantity as pQuantity, cart"+ String.valueOf(customer.getId()) +".quantity from products inner join cart"+ String.valueOf(customer.getId()) + " on products.pid = cart" + String.valueOf(customer.getId()) +".productId";
        ObservableList<Cart> list = getCart(searchQuery);
        ECommerce.getPage(list.size());
        return list;
    }

    public static ObservableList<Cart> getCart(String query){
        DatabaseConnection dbConn = new DatabaseConnection();
        ResultSet rs = dbConn.getQueryTable(query);
        ObservableList<Cart> result = FXCollections.observableArrayList();
        try{
            if (rs != null) {
                while (rs.next()){
                    //taking out values from resultSet
                    result.add(new Cart(
                                    rs.getInt("cid"),
                                    rs.getInt("pid"),
                                    rs.getString("name"),
                                    rs.getDouble("price"),
                                    rs.getInt("quantity"),
                                    rs.getInt("pQuantity")
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

    public static boolean addToCart(Customer customer, Product product, int quantity){
        String tableName = "cart" + String.valueOf(customer.getId());
        int productId = product.getId();
        try{
            //INSERT INTO `ecomm`.`yugalrajdeka@gmail.com` (`pid`, `quantity`) VALUES ('1', '2');
            String addCart = "INSERT INTO `ecomm`.`" + tableName +"` (`productId`, `quantity`) VALUES ('"+ String.valueOf(productId) +"', '"+ String.valueOf(quantity) +"');";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(addCart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
  //UPDATE `ecomm`.`cart3` SET `quantity` = '2' WHERE (`productId` = '4');
    public static boolean updateCart(Customer customer, int productId, int quantity){
        String tableName = "cart" + String.valueOf(customer.getId());
        try{
            //UPDATE `ecomm`.`cart3` SET `quantity` = '2' WHERE (`productId` = '4');
            String addCart = "UPDATE `ecomm`.`"+ tableName +"` SET `quantity` = '"+ String.valueOf(quantity) +"' WHERE (`productId` = '"+ String.valueOf(productId) +"')";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(addCart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeAll(Customer customer){
        try{
            String deleteAllQuery = "DELETE FROM `ecomm`.`cart"+ String.valueOf(customer.getId()) +"`";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(deleteAllQuery);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean remove(Customer customer, int cartId){
        try{
            //DELETE FROM `ecomm`.`cart3` WHERE (`productId` = '3');
//            DELETE FROM `ecomm`.`cart2` WHERE (`cid` = '2');
            ECommerce.showDialogue("DELETE FROM `ecomm`.`cart"+ String.valueOf(customer.getId()) +"` WHERE (`cid` = '" + cartId +"');");
            String deleteAllQuery = "DELETE FROM `ecomm`.`cart"+ String.valueOf(customer.getId()) +"` WHERE (`cid` = '" + cartId +"');";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(deleteAllQuery);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
