package com.example.ecommerce;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.ResultSet;

public class Login {

    private static byte[] getSha(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    static String getEncryptedPassword(String password){
        try{
            BigInteger num = new BigInteger(1, getSha(password));
            StringBuilder hexString = new StringBuilder(num.toString(16));
            return hexString.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public  static Customer customerLogin(String userEmail, String password){
        String encryptedPass = getEncryptedPassword(password);
        String query = "SELECT * FROM customer WHERE email = '" + userEmail +"' and password = '" + encryptedPass + "'";
        DatabaseConnection dbConn = new DatabaseConnection();
        try{
            ResultSet rs = dbConn.getQueryTable(query);
            if(rs != null && rs.next())
                return new Customer(
                        rs.getInt("cid"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getString("password"),
                        rs.getString("address")
                );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean newCustomerLogin(String name, String email, String address, String mobile, String password){
//        insert into customer(name, email, mobile, address, password) values('Swatha Das', 'swatha@gmail.com','123','Guwahati','hello');
        try{
            /*CREATE TABLE `ecomm`.`cart2` (`cid` INT NOT NULL AUTO_INCREMENT,`productId` INT NOT NULL, `quantity` INT NOT NULL, PRIMARY KEY (`cid`));*/
            String encryptedPass = getEncryptedPassword(password);
            String addCustomer = "INSERT INTO customer(name, email, mobile, address, password) VALUES('" + name + "','" + email + "','" + mobile + "','" + address + "','" + encryptedPass +"');";

            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(addCustomer);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public static void newCart(int customerId){
        try{
            /*CREATE TABLE `ecomm`.`cart2` (`cid` INT NOT NULL AUTO_INCREMENT,`productId` INT NOT NULL, `quantity` INT NOT NULL, PRIMARY KEY (`cid`));*/
            String createCart = "CREATE TABLE `ecomm`.`cart" + String.valueOf(customerId) + "` (`cid` INT NOT NULL AUTO_INCREMENT,`productId` INT NOT NULL, `quantity` INT NOT NULL, PRIMARY KEY (`cid`))";
            DatabaseConnection dbConn = new DatabaseConnection();
            dbConn.insertUpdateCreate(createCart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean changePassword(int customerId, String password){
        //UPDATE `ecomm`.`customer` SET `password` = '2cf24dba5fb0a30e26e83b2ac5b9e29e1161e5c1fa7425e73043362938b9824' WHERE (`cid` = '3');
        try{
            String newEncryptedPass = getEncryptedPassword(password);
            String createCart = "UPDATE `ecomm`.`customer` SET `password` = '" + newEncryptedPass + "' WHERE (`cid` = '" + String.valueOf(customerId) +"')";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(createCart);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAccount(int customerId){
        //DELETE FROM `ecomm`.`customer` WHERE (`cid` = '1');
        //DROP TABLE `ecomm`.`cart1`
        try{
            String deleteCustomer = "DELETE FROM `ecomm`.`customer` WHERE (`cid` = '" + String.valueOf(customerId) + "')";
            String deleteCart = "DROP TABLE `ecomm`.`cart" + String.valueOf(customerId) + "`";
            DatabaseConnection dbConn = new DatabaseConnection();
            return dbConn.insertUpdateCreate(deleteCustomer) & dbConn.insertUpdateCreate(deleteCart);
        }
        catch (Exception e){
            e.printStackTrace();;
        }
        return false;
    }

/*    public static void main(String[] args) {
        System.out.println(customerLogin("angad@gmail.com", "abc"));
        System.out.println(getEncryptedPassword("hello"));
    }*/
}
