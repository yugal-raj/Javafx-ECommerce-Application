package com.example.ecommerce;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CartList {
    public static TableView<Cart> cartTable;
    public static GridPane getAllCart(Customer loggedInCustomer, String cartStatus){

        ObservableList<Cart> orderList = Cart.getAllCart(loggedInCustomer);
        cartTable = new TableView<>();
        cartTable.setItems(orderList);


        GridPane tablePane = new GridPane();
        int cartTableSize = cartTable.getItems().size();
        int[] quantity = new int[cartTableSize];
        ScrollPane scroll = new ScrollPane();

        if(cartTableSize == 0){
            GridPane emptyCartPane = new GridPane();
            Text emptyCartMessage = new Text("Cart is Empty");
            emptyCartMessage.setFont(Font.font(15));
            emptyCartMessage.setFill(Color.GRAY);
            emptyCartPane.getChildren().add(emptyCartMessage);
            scroll.setContent(emptyCartPane);
            emptyCartPane.setTranslateX(330);
            emptyCartPane.setTranslateY(130);
        }
        else {
            for (int i = 0; i < cartTableSize; i++) {
                GridPane gp = new GridPane();
                Cart cart = cartTable.getItems().get(i);

                if (cartStatus.equals("buyAll")) {
                    ObservableList<Product> productQuantityList = Product.getProductDetails(cart.getProductId());
                    TableView<Product> productQuantityTable = new TableView<>();
                    productQuantityTable.setItems(productQuantityList);
                    Product product = productQuantityTable.getItems().get(0);
//                ECommerce.showDialogue(String.valueOf(i+1));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
//                ECommerce.showDialogue("product name : "+ product.getName()+"\nproduct quantity " + product.getQuantity() + "\ncart product: " + cart.getProductName() + "\n cart quantity: " + cart.getQuantity());
                    if (product.getQuantity() == 0)
                        ECommerce.showDialogue(cart.getProductName() + " is Out of Stock");
                    if (product.getQuantity() > cart.getQuantity()) {
                        boolean orderStatus = Order.placeOrder(loggedInCustomer, product, cart.getQuantity(), formatter.format(date));
                        if (orderStatus) {
                            Cart.remove(loggedInCustomer, cart.getCartId());
                        } else
                            ECommerce.showDialogue("failed to order");
                    }
                } else {
                    quantity[i] = cart.getQuantity();

                    InputStream stream;

                    try {
                        stream = new FileInputStream(cart.getImageLocation());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    Image image = new Image(stream);
                    //Creating the image view
                    ImageView imageView = new ImageView();
                    //Setting image to the image view
                    imageView.setImage(image);
                    imageView.setFitHeight(60);
                    imageView.setFitWidth(70);
                    GridPane imagePane = new GridPane();
                    imagePane.getChildren().add(imageView);

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
                    updateButton.setOnAction(actionEvent -> {
                        if (quantity[finalI] == 0) {
                            Cart.remove(loggedInCustomer, cart.getCartId());
                            CustomerProfile customerProfile = new CustomerProfile();
                            GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                            ECommerce.bodyPane.getChildren().clear();
                            ECommerce.bodyPane.getChildren().add(cartPane);
                            cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                            cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                        } else {
                            boolean updateStatus;
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
                    });

                    incrementButton.setOnAction(actionEvent -> {
                        if (quantity[finalI] < cart.getProductQuantity()) {
                            quantity[finalI]++;
                            productQuantity.setText(String.valueOf(quantity[finalI]));
                            productPrice.setText(String.valueOf(quantity[finalI] * cart.getPrice()));
                            gp.getChildren().remove(updateButton);
                            gp.add(updateButton, 5, 0);
                        }
                    });
                    decrementButton.setOnAction(actionEvent -> {
                        if (quantity[finalI] > 0) {
                            quantity[finalI]--;
                            productQuantity.setText(String.valueOf(quantity[finalI]));
                            productPrice.setText(String.valueOf(quantity[finalI] * cart.getPrice()));
                            gp.getChildren().remove(updateButton);
                            gp.add(updateButton, 5, 0);
                        }
                    });

                    GridPane pricePane = new GridPane();
                    pricePane.getChildren().add(productPrice);

                    Button removeButton = new Button("Remove");
                    removeButton.setOnAction(actionEvent -> {
                        boolean removeStatus;
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
                    });

                    gp.add(imagePane, 0, 0);
                    gp.add(namePane, 1, 0);
                    gp.add(quantityPane, 2, 0);
                    gp.add(pricePane, 3, 0);
                    gp.add(removeButton, 4, 0);
                    gp.setHgap(10);
//                gp.add(updateButton, 5, 0);
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
                    Text quantityAlertText = new Text();
                    if (cart.getProductQuantity() <= 50) {
                        quantityAlertText.setText("Only " + cart.getProductQuantity() + " in stock");
                    }
                    if (cart.getProductQuantity() == 0) {
                        quantityAlertText.setText("Out of Stock");
                    }
                    quantityAlertText.setFill(Color.RED);
                    quantityAlertText.setFont(Font.font(10));
                    alertPane.getChildren().add(quantityAlertText);
                    alertPane.setTranslateX(420);
                    alertPane.setTranslateY(40);
                    alertPane.setPrefWidth(50);
                    gp.getChildren().add(alertPane);
                    tablePane.add(gp, 0, i);

                }
            }
            if (cartStatus.equals("buyAll")) {
                CustomerProfile customerProfile = new CustomerProfile();
                GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                ECommerce.bodyPane.getChildren().clear();
                ECommerce.bodyPane.getChildren().add(cartPane);
                cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
            }
//        tablePane.getChildren().add(cartTable);
            tablePane.setPrefWidth(2000);
            scroll.setContent(tablePane);
        }
        scroll.setTranslateX(150);
        scroll.setTranslateY(100);
        scroll.setPrefSize(700, 300);
        scroll.setFitToWidth(true);
        GridPane finalPane = new GridPane();
        finalPane.add(scroll, 0, 0);
        Button removeAllButton = new Button("Remove All");
        Button buyAllButton = new Button("Buy All");
        GridPane buttonPane = new GridPane();
        buttonPane.add(removeAllButton, 0, 0);
        buttonPane.add(buyAllButton, 1, 0);
        finalPane.add(buttonPane, 1, 0);
        buttonPane.setHgap(20);
        buttonPane.setTranslateY(430);

        removeAllButton.setOnAction(actionEvent -> {
            boolean removeStatus;
            removeStatus = Cart.removeAll(loggedInCustomer);
            if(removeStatus) {
                CustomerProfile customerProfile = new CustomerProfile();
                GridPane cartPane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "cart");
                ECommerce.bodyPane.getChildren().clear();
                ECommerce.bodyPane.getChildren().add(cartPane);
                cartPane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                cartPane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
            }
        });

        buyAllButton.setOnAction(actionEvent -> getAllCart(loggedInCustomer, "buyAll"));

        return finalPane;
    }
}
