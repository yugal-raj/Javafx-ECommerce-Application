package com.example.ecommerce;
import java.sql.*;

public class DatabaseConnection {

    String dbURL = "jdbc:mysql://localhost:3306/ecomm";
    String userName = "root";
    String password = "paradigmshift";

    private Statement getStatement(){
        try{
            Connection conn = DriverManager.getConnection(dbURL, userName, password);
            return conn.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getQueryTable(String query){
        Statement statement = getStatement();
        try {
            assert statement != null;
            return statement.executeQuery(query);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUpdateCreate(String query){
        Statement statement = getStatement();
        try {
            assert statement != null;
            statement.executeUpdate(query);
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

//    /*public static void main(String[] args) {
//        String query  = "SELECT * FROM products";
//        DatabaseConnection dbConn = new DatabaseConnection();
//        ResultSet rs = dbConn.getQueryTable(query);
//        if(rs != null){
//            System.out.println("Connected to Database");
//        }
//        else
//            System.out.println("hello ");
//    }*/
}
