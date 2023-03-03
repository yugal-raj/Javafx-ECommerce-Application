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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProductList {
    public TableView<Product> productTable;
    private  Customer customer;
    public GridPane getAllProducts(Customer loggedInCustomer, String searchText, int page){
        customer = loggedInCustomer;

        TableColumn id = new TableColumn("Id")/*name = new TableColumn("Name"), price = new TableColumn("Price")*/;
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        price.setCellValueFactory(new PropertyValueFactory<>("price"));



        ObservableList<Product> data = FXCollections.observableArrayList();


        ObservableList<Product> productList = Product.getAllProducts(searchText, page);
        productTable = new TableView<>();
        productTable.setItems(productList);
        productTable.getColumns().add(id);


        GridPane tablePane = new GridPane();
        int i = 0;
        while(i < productTable.getItems().size()){
            GridPane gp = new GridPane();
            Product p = productTable.getItems().get(i);
            InputStream stream = null;

            try {
                stream = new FileInputStream(p.getImageLocation());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Image image = new Image(stream);
            //Creating the image view
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            imageView.setFitHeight(160);
            imageView.setFitWidth(250);
            Text text = new Text(p.getName());
            Text price = new Text(String.valueOf(p.getPrice()));
            GridPane imagePane = new GridPane();
            imagePane.getChildren().add(imageView);
            gp.add(imagePane, 0, 0);
            imagePane.setAlignment(Pos.CENTER);
            GridPane textPane = new GridPane();
            textPane.add(text, 0, 0);
            textPane.add(price, 0, 1);
            gp.add(textPane, 0, 1);
            textPane.setOnMouseClicked(e->{
                fetchDetails(tablePane, p);
            });
            gp.setStyle("-fx-padding: 10;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: black;" +
                    "-fx-background-color: white");
            textPane.setAlignment(Pos.CENTER);
            Button button = new Button("");
            button.setPrefHeight(200);
            button.setPrefWidth(320);
            button.setStyle("-fx-background-color: transparent");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    fetchDetails(tablePane, p);
                }
            });
            gp.getChildren().add(button);
            tablePane.add(gp, i%3, i/3);
            i++;
        }
        while(i < 6){
            GridPane gp = new GridPane();
            GridPane imagePane = new GridPane();
            gp.add(imagePane, 0, 0);
            imagePane.setAlignment(Pos.CENTER);
            imagePane.setPrefHeight(160);
            imagePane.setPrefWidth(250);
            Text text = new Text("Dummy");
            text.setFill(Color.BEIGE);
            GridPane textPane = new GridPane();
            textPane.getChildren().add(text);
            gp.add(textPane, 0, 1);
            gp.setStyle("-fx-padding: 10;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: transparent;" +
                    "-fx-background-color: transparent");
            textPane.setAlignment(Pos.CENTER);
            tablePane.add(gp, i%3, i/3);
            i++;
        }
        tablePane.setAlignment(Pos.CENTER);
        tablePane.setVgap(30);
        tablePane.setHgap(30);
        tablePane.setTranslateY(20);
        tablePane.setAlignment(Pos.CENTER_LEFT);
//        tablePane.setEffect(new DropShadow());
        return tablePane;
    }

//    public Product getSelectedProduct(){
//        return productTable.getSelectionModel().getSelectedItem();
//    }

    private void fetchDetails(GridPane tablePane, Product p){
        tablePane.getChildren().clear();
        tablePane.getChildren().add(ProductDetails.getDetails(p, customer));
        ECommerce.root.setBottom(null);
    }
}
