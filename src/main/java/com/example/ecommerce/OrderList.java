package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderList {
    public static TableView<Order> orderTable;
    static int page = 1;
    public static GridPane getAllOrders(Customer loggedInCustomer){

        TableColumn  pName = new TableColumn("Product Name");
        pName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        ObservableList<Order> data = FXCollections.observableArrayList();


        ObservableList<Order> orderList = Order.getAllOrders(loggedInCustomer);
        orderTable = new TableView<>();
        orderTable.setItems(orderList);
        int orderTableSize = orderTable.getItems().size();
//        orderTable.getColumns().add(pName);

        GridPane displayPane = new GridPane();
        if(orderTableSize == 0){
            Text emptyTableText = new Text("No Orders Found");
            emptyTableText.setFont(Font.font(15));
            displayPane.getChildren().add(emptyTableText);
            displayPane.setTranslateY(200);
            displayPane.setTranslateX(450);
        }
        else {
            GridPane pagePane = new GridPane();
            Button decrementButton = new Button("<");
            Button incrementButton = new Button(">");
            Text pageText = new Text(String.valueOf(page));
            pagePane.add(decrementButton, 0, 0);
            pagePane.add(pageText, 1, 0);
            pagePane.add(incrementButton, 2, 0);
            pagePane.setHgap(20);

            decrementButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (page > 1) {
                        page--;
                        pageText.setText(String.valueOf(page));
                        displayPane.add(getTablePane(page), 0, 0);
                    }
                }
            });
            incrementButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (page < orderTableSize / 4 + (orderTableSize % 4 > 0 ? 1 : 0)) {
                        page++;
                        pageText.setText(String.valueOf(page));
                        displayPane.getChildren().clear();
                        displayPane.add(getTablePane(page), 0, 0);
                        displayPane.getChildren().add(pagePane);
                    }
                }
            });

            displayPane.add(getTablePane(page), 0, 0);
            displayPane.getChildren().add(pagePane);
            pagePane.setTranslateY(530);
            pagePane.setTranslateX(460);
        }
        return displayPane;
    }

    static GridPane getTablePane(int page){

        GridPane tablePane = new GridPane();
        for(int i = (page-1)*4; i<(page-1)*4+4 && i <orderTable.getItems().size(); i++) {
            GridPane gp = new GridPane();
            Order order = orderTable.getItems().get(i);

            InputStream stream = null;

            try {
                stream = new FileInputStream(order.getProductImageLocation());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Image image = new Image(stream);
            //Creating the image view
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            GridPane imagePane = new GridPane();
            imagePane.getChildren().add(imageView);

            int quantity = order.getQuantity();
            Text productName = new Text(String.valueOf(order.getProductName()));
            Text productQuantity = new Text(String.valueOf(quantity));
            Text productPrice = new Text(String.valueOf(order.getPrice() * quantity));

            GridPane namePane = new GridPane();
            namePane.getChildren().add(productName);
            GridPane quantityPane = new GridPane();

            GridPane numPane = new GridPane();
            numPane.getChildren().add(productQuantity);
            Label quantityLabel = new Label("QUANTITY");
            quantityLabel.setFont(Font.font(10));
            quantityPane.add(quantityLabel, 0, 0);
            quantityPane.add(numPane, 0, 1);
            numPane.setPrefWidth(30);
            numPane.setAlignment(Pos.CENTER);

            GridPane pricePane = new GridPane();
            Label amountLabel = new Label("TOTAL AMOUNT");
            amountLabel.setFont(Font.font(10));
            pricePane.add(amountLabel, 0, 0);
            pricePane.add(productPrice, 0, 1);

            GridPane datePane = new GridPane();
            GridPane timePane = new GridPane();
            Text dateText = new Text();
            Text timeText = new Text();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date productDate;
            try {
                productDate = formatter.parse(order.getDateTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String dateString = formatter1.format(productDate);
            String[] date = dateString.split(" ");
            dateText.setText(date[0]);
            timeText.setText(date[1]);
            Label dateLabel = new Label("DATE");
            dateLabel.setFont(Font.font(10));
            Label timeLabel = new Label("TIME");
            timeLabel.setFont(Font.font(10));
            datePane.add(dateLabel, 0, 0);
            datePane.add(dateText, 0, 1);
            timePane.add(timeLabel, 0, 0);
            timePane.add(timeText, 0, 1);
            datePane.setAlignment(Pos.CENTER);
            timePane.setAlignment(Pos.CENTER);
//            datePane.setTranslateX(20);
//            timePane.setTranslateX(40);

            gp.add(imagePane, 0, 0);
            gp.add(namePane, 1, 0);
            gp.add(quantityPane, 2, 0);
            gp.add(pricePane, 3, 0);
            gp.add(datePane, 4, 0);
            gp.add(timePane, 5, 0);
//                gp.add(updateButton, 4, 0);
            gp.setStyle("-fx-padding: 10;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: black;" +
                    "-fx-background-color: white");
            gp.setHgap(30);
            namePane.setAlignment(Pos.CENTER);
            namePane.setPrefWidth(200);
            quantityPane.setAlignment(Pos.CENTER);
            quantityPane.setPrefWidth(100);
            pricePane.setAlignment(Pos.CENTER);
            pricePane.setPrefWidth(100);
            gp.setPrefWidth(1700);
            gp.setPrefHeight(70);
            tablePane.add(gp, 0, i);
        }
        tablePane.setAlignment(Pos.CENTER);
        tablePane.setTranslateY(100);
        tablePane.setTranslateX(80);
        tablePane.setPrefWidth(800);
        return tablePane;
    }
}
