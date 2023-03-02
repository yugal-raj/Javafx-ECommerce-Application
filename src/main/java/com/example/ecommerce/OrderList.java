package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OrderList {
    public static TableView<Order> orderTable;
    public static GridPane getAllOrders(Customer loggedInCustomer){

        TableColumn  pName = new TableColumn("Product Name"), imageLoaction = new TableColumn("Image Location"), date = new TableColumn("Date"), quantity = new TableColumn("Quantity");
        pName.setCellValueFactory(new PropertyValueFactory<>("productName"));
//        imageLoaction.setCellValueFactory(new PropertyValueFactory<>("productImageLocation"));
        date.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        ObservableList<Order> data = FXCollections.observableArrayList();


        ObservableList<Order> orderList = Order.getAllOrders(loggedInCustomer);
        orderTable = new TableView<>();
        orderTable.setItems(orderList);
        orderTable.getColumns().add(pName);
//        orderTable.getColumns().add(imageLoaction);
        orderTable.getColumns().add(date);
        orderTable.getColumns().add(quantity);


        GridPane tablePane = new GridPane();
//        int i = 0;
//        while(i<orderTable.getItems().size()){
//            GridPane gp = new GridPane();
//            Order order = orderTable.getItems().get(i);
////            InputStream stream = null;
////
////            try {
////                stream = new FileInputStream(p.getImageLocation());
////            } catch (FileNotFoundException e) {
////                throw new RuntimeException(e);
////            }
//
////            Image image = new Image(stream);
////            //Creating the image view
////            ImageView imageView = new ImageView();
////            //Setting image to the image view
////            imageView.setImage(image);
////            imageView.setFitHeight(160);
////            imageView.setFitWidth(250);
//            Text productId = new Text(String.valueOf(order.getProductName()));
//            Text orderDate = new Text(order.getDateTime());
////            text.setOnMouseClicked(e->{
////                fetchDetails(tablePane, p);
////            });
////            GridPane imagePane = new GridPane();
////            imagePane.getChildren().add(imageView);
////            gp.add(imagePane, 0, 0);
////            imagePane.setAlignment(Pos.CENTER);
//            GridPane namePane = new GridPane();
//            namePane.getChildren().add(productId);
//            GridPane datePane = new GridPane();
//            datePane.getChildren().add(orderDate);
//            gp.add(namePane, 0, 0);
//            gp.add(datePane, 1, 0);
//            gp.setStyle("-fx-padding: 10;" +
//                    "-fx-border-style: solid inside;" +
//                    "-fx-border-width: 2;" +
//                    "-fx-border-insets: 5;" +
//                    "-fx-border-radius: 5;" +
//                    "-fx-border-color: black;" +
//                    "-fx-background-color: white");
//            namePane.setAlignment(Pos.CENTER);
//            datePane.setAlignment(Pos.CENTER);
////            Button button = new Button("");
////            button.setPrefHeight(200);
////            button.setPrefWidth(320);
////            button.setStyle("-fx-background-color: transparent");
////            button.setOnAction(new EventHandler<ActionEvent>() {
////                @Override
////                public void handle(ActionEvent actionEvent) {
////                    fetchDetails(tablePane, p);
////                }
////            });
////            gp.getChildren().add(button);
//            tablePane.add(gp, 0, i);
//            i++;
//        }
        tablePane.getChildren().add(orderTable);
        tablePane.setAlignment(Pos.CENTER);
        orderTable.setPrefWidth(400);
        tablePane.setTranslateY(100);
        tablePane.setPrefWidth(800);
        return tablePane;
    }
}
