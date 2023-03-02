package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CartList {
    public static TableView<Cart> cartTable;
    public static GridPane getAllCart(Customer loggedInCustomer, String cartStatus){

        TableColumn pName = new TableColumn("Product Name");
        pName.setCellValueFactory(new PropertyValueFactory<>("productName"));



        ObservableList<Cart> data = FXCollections.observableArrayList();


        ObservableList<Cart> orderList = Cart.getAllCart(loggedInCustomer);
        cartTable = new TableView<>();
        cartTable.setItems(orderList);
        cartTable.getColumns().add(pName);


        GridPane tablePane = new GridPane();
        int n = cartTable.getItems().size();
        int quantity[] = new int[n];
        for(int i = 0; i<cartTable.getItems().size(); i++){
            GridPane gp = new GridPane();
            Cart cart = cartTable.getItems().get(i);

            if(cartStatus.equals("buyAll")){
                boolean orderStatus = false;
                Product product = new Product();
                product.setId(cart.getProductId());
                product.setQuantity(cart.getProductQuantity());
//                ECommerce.showDialogue(String.valueOf(i+1));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                orderStatus = Order.placeOrder(loggedInCustomer, product, cart.getQuantity(), formatter.format(date));
                if(orderStatus) {
                    boolean removeStatus = false;
                    removeStatus = Cart.remove(loggedInCustomer, cart.getCartId());
                    if(removeStatus)
                        ECommerce.showDialogue("Removed");
                    else
                        ECommerce.showDialogue("not removed");
                }
                else
                    ECommerce.showDialogue(cart.getProductName() + " is Out of Stock");
            }
            else {
                quantity[i] = cart.getQuantity();
                Text productId = new Text(String.valueOf(cart.getProductName()));
                Text productQuantity = new Text(String.valueOf(quantity[i]));
                Text productPrice = new Text(String.valueOf(cart.getPrice() * quantity[i]));

                GridPane namePane = new GridPane();
                namePane.getChildren().add(productId);
                GridPane quantityPane = new GridPane();

                Button incrementButton = new Button(">");
                Button decrementButton = new Button("<");
                GridPane numPane = new GridPane();
                numPane.getChildren().add(productQuantity);
                quantityPane.add(decrementButton, 0, 0);
                quantityPane.add(numPane, 1, 0);
                quantityPane.add(incrementButton, 2, 0);
                numPane.setPrefWidth(30);
                numPane.setAlignment(Pos.CENTER);

                int finalI = i;

                Button updateButton = new Button("Update");
                updateButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(quantity[finalI] == 0){
                            Cart.remove(loggedInCustomer, cart.getCartId());
                            CustomerProfile customerProfile = new CustomerProfile();
                            GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                            ECommerce.bodyPane.getChildren().clear();
                            ECommerce.bodyPane.getChildren().add(cartPane);
                            cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                            cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                        }
                        else {
                            boolean updateStatus = false;
                            updateStatus = Cart.updateCart(loggedInCustomer, cart.getProductId(), quantity[finalI]);
                            if (updateStatus) {
                                ECommerce.showDialogue("Updated");
                                CustomerProfile customerProfile = new CustomerProfile();
                                GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                                ECommerce.bodyPane.getChildren().clear();
                                ECommerce.bodyPane.getChildren().add(cartPane);
                                cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                                cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                            } else
                                ECommerce.showDialogue("Not updated");
                        }
                    }
                });

                incrementButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (quantity[finalI] < cart.getProductQuantity()) {
                            quantity[finalI]++;
                            productQuantity.setText(String.valueOf(quantity[finalI]));
                            productPrice.setText(String.valueOf(quantity[finalI] * cart.getPrice()));
                            gp.getChildren().remove(updateButton);
                            gp.add(updateButton, 4, 0);
                        }
                    }
                });
                decrementButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (quantity[finalI] > 0) {
                            quantity[finalI]--;
                            productQuantity.setText(String.valueOf(quantity[finalI]));
                            productPrice.setText(String.valueOf(quantity[finalI] * cart.getPrice()));
                            gp.getChildren().remove(updateButton);
                            gp.add(updateButton, 4, 0);
                        }
                    }
                });

                GridPane pricePane = new GridPane();
                pricePane.getChildren().add(productPrice);

                Button removeButton = new Button("Remove");
                removeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        boolean removeStatus = false;
                        removeStatus = Cart.remove(loggedInCustomer, cart.getCartId());
                        if (removeStatus) {
                            CustomerProfile customerProfile = new CustomerProfile();
                            GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                            ECommerce.bodyPane.getChildren().clear();
                            ECommerce.bodyPane.getChildren().add(cartPane);
                            cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                            cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                        } else
                            ECommerce.showDialogue("failed to remove");
                    }
                });

                gp.add(namePane, 0, 0);
                gp.add(quantityPane, 1, 0);
                gp.add(pricePane, 2, 0);
                gp.add(removeButton, 3, 0);
//                gp.add(updateButton, 4, 0);
                gp.setStyle("-fx-padding: 10;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: black;" +
                        "-fx-background-color: white");
                namePane.setAlignment(Pos.CENTER);
                namePane.setPrefWidth(200);
                quantityPane.setAlignment(Pos.CENTER);
                quantityPane.setPrefWidth(100);
                pricePane.setAlignment(Pos.CENTER);
                pricePane.setPrefWidth(100);
                gp.setPrefWidth(2000);
                gp.setPrefHeight(70);
                GridPane alertPane = new GridPane();
                Text t = new Text();
                if(cart.getProductQuantity() <= 50){
                    t.setText("Only " + String.valueOf(cart.getProductQuantity()) + " in stock");
                }
                t.setFill(Color.RED);
                t.setFont(Font.font(10));
                alertPane.getChildren().add(t);
                alertPane.setTranslateX(400);
                alertPane.setTranslateY(27);
                alertPane.setPrefWidth(50);
                gp.getChildren().add(alertPane);
                tablePane.add(gp, 0, i);

            }
        }
        if(cartStatus.equals("buyAll")){
            CustomerProfile customerProfile = new CustomerProfile();
            GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
            ECommerce.bodyPane.getChildren().clear();
            ECommerce.bodyPane.getChildren().add(cartPane);
            cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
            cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
        }
//        tablePane.getChildren().add(cartTable);
        tablePane.setPrefWidth(2000);
        ScrollPane scroll = new ScrollPane();
        scroll.setTranslateX(200);
        scroll.setTranslateY(150);
        scroll.setPrefSize(600, 200);
        scroll.setFitToWidth(true);
        scroll.setContent(tablePane);
        GridPane finalPane = new GridPane();
        finalPane.add(scroll, 0, 0);
        Button removeAllButton = new Button("Remove All");
        Button buyAllButton = new Button("Buy All");
        finalPane.add(removeAllButton, 1, 0);
        finalPane.add(buyAllButton, 2, 0);
        removeAllButton.setTranslateY(400);
        buyAllButton.setTranslateY(400);

        removeAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean removeStatus = false;
                removeStatus = Cart.removeAll(loggedInCustomer);
                if(removeStatus) {
                    CustomerProfile customerProfile = new CustomerProfile();
                    GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                    ECommerce.bodyPane.getChildren().clear();
                    ECommerce.bodyPane.getChildren().add(cartPane);
                    cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                    cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                }
            }
        });

        buyAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getAllCart(loggedInCustomer, "buyAll");
            }
        });

        return finalPane;
    }
}
