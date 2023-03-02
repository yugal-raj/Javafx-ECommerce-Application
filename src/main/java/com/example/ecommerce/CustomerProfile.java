package com.example.ecommerce;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerProfile {
    private Customer customer;
    private GridPane profilePane;
    private GridPane orderPane;
    private GridPane cartPane;

    private GridPane privacyPane;

    public GridPane getCustomerProfile(Customer customer, Label welcomeLabel, String menuOption) {
        this.customer = customer;
        ECommerce.root.setBottom(null);
//        ECommerce.showDialogue("mobile: " + customer.getMobile() + "\npassword: " + customer.getPassword());
        GridPane customerProfilePane = new GridPane();
        GridPane menuPane = new GridPane();
        GridPane displayPane = new GridPane();

        menuPane.getChildren().add(customerDetails(menuPane, displayPane, 200, welcomeLabel, menuOption));
        menuPane.getChildren().add(customerOrders(menuPane, displayPane, 240, menuOption));
        menuPane.getChildren().add(customerCart(menuPane, displayPane, 280, menuOption));
        menuPane.getChildren().add(customerPrivacy(menuPane, displayPane, 320));

        menuPane.setStyle("-fx-background-color: white");
        customerProfilePane.add(menuPane, 0, 0);
        customerProfilePane.add(displayPane, 1, 0);
//        if(menuOption.equals("default")){
//            ECommerce.showDialogue("default");
////            customerDetailsPane.setStyle("-fx-background-color: gray");
//            displayPane.getChildren().clear();
//            displayPane.getChildren().add(printDetails(displayPane, 160, welcomeLabel));
//        }
        menuPane.setPrefWidth(300);
        displayPane.prefHeightProperty().bind(customerProfilePane.heightProperty());
//        displayPane.setStyle("-fx-background-color: blue");
        displayPane.setPrefWidth(1000);
//        customerProfilePane.setAlignment(Pos.CENTER_LEFT);
        return customerProfilePane;
    }

    private GridPane customerDetails(GridPane menuPane, GridPane displayPane, int yTranslate, Label welcomeLabel, String menuOption) {
        profilePane = new GridPane();
        profilePane.prefWidthProperty().bind(menuPane.widthProperty());
        profilePane.setPrefHeight(40);

        profilePane.setTranslateY(yTranslate);
        Text text = new Text("Profile");
        profilePane.getChildren().add(text);
        profilePane.setAlignment(Pos.CENTER);
        if(menuOption.equals("default")){
            profilePane.setStyle("-fx-background-color: beige");
            displayPane.getChildren().clear();
            displayPane.getChildren().add(printDetails(displayPane, 160, welcomeLabel));
        }
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                profilePane.setStyle("-fx-background-color: beige");
                orderPane.setStyle("-fx-background-color: white");
                cartPane.setStyle("-fx-background-color: white");
                privacyPane.setStyle("-fx-background-color: white");
                displayPane.getChildren().clear();
                displayPane.getChildren().add(printDetails(displayPane, 160, welcomeLabel));
            }
        });
        return profilePane;
    }

    private GridPane printDetails(GridPane displayPane, int yTranslate, Label welcomeLabel) {
        GridPane details = new GridPane();
        details.prefWidthProperty().bind(displayPane.widthProperty());
        ArrayList<String> components = new ArrayList<>(Arrays.asList("name", "email", "mobile", "address"));
        for(int i = 0; i<components.size(); i++){
            details.getChildren().add(getProfileDetails(details, yTranslate + (i*40), welcomeLabel, components.get(i)));
        }
        return details;
    }
    private GridPane getProfileDetails(GridPane details, int yTranslate, Label welcomeLabel, String column) {

        GridPane namePane = new GridPane();
        Label nameText = new Label();
        GridPane editPane = new GridPane();
        Label name = new Label();
        name.setWrapText(true);
        nameText.setWrapText(true);
        if(column.equals("name")) {
            nameText.setText("Name");
            nameText.setFont(Font.font(15));
            name.setText(customer.getName());
//        name.prefWidth(300);
            name.setFont(Font.font(15));
        }
        if(column.equals("email")) {
            nameText.setText("Email");
            nameText.setFont(Font.font(15));
            name.setText(customer.getEmail());
    //        name.prefWidth(300);
            name.setFont(Font.font(15));
        }
        if(column.equals("mobile")) {
            nameText.setText("Mobile");
            nameText.setFont(Font.font(15));
            name.setText(customer.getMobile());
    //        name.prefWidth(300);
            name.setFont(Font.font(15));
        }
        if(column.equals("address")) {
            nameText.setText("Address");
            nameText.setFont(Font.font(15));
            name.setText(customer.getAddress());
    //        name.prefWidth(300);
            name.setFont(Font.font(15));
        }
        editPane.getChildren().add(name);
        Button editNameButton = new Button("Edit");
        namePane.add(nameText, 0, 0);
        namePane.add(editPane, 1, 0);
        namePane.add(editNameButton, 2, 0);
        TextField editName = new TextField();
        Button saveNameButton = new Button("Save");
        Button cancelNameButton = new Button("Cancel");
//        editName.setPrefWidth(300);

        editNameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editPane.getChildren().clear();
                editPane.getChildren().add(editName);
                editName.setPrefWidth(300);
                namePane.getChildren().remove(editNameButton);
                namePane.add(saveNameButton, 2, 0);
                namePane.add(cancelNameButton, 3, 0);
            }
        });

        cancelNameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editPane.getChildren().clear();
                editPane.getChildren().add(name);
                namePane.getChildren().removeAll(saveNameButton, cancelNameButton);
                namePane.add(editNameButton, 2, 0);
            }
        });

        saveNameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //UPDATE `ecomm`.`customer` SET `name` = 'Raj Deka' WHERE (`cid` = '3');
                if(editName.getText().isEmpty())
                    ECommerce.showDialogue("Field is empty");
                else {
                    String updateName = editName.getText();
                    String query = "UPDATE `ecomm`.`customer` SET `" + column + "` = '" + updateName + "' WHERE (`cid` = '" + String.valueOf(customer.getId()) + "');";
                    DatabaseConnection dbConn = new DatabaseConnection();
                    boolean updateStatus = dbConn.insertUpdateCreate(query);
                    if (updateStatus) {
                        customer.setName(updateName);
                        name.setText(updateName);
                        editPane.getChildren().clear();
                        editPane.getChildren().add(name);
                        namePane.getChildren().removeAll(saveNameButton, cancelNameButton);
                        namePane.add(editNameButton, 2, 0);
                        if (column.equals("name"))
                            welcomeLabel.setText("Welcome " + updateName);
                    } else {
                            ECommerce.showDialogue(updateName + " is already assigned to an existing account");
                    }
                }
            }
        });

        editPane.setPrefWidth(300);
        if(column.equals("email"))
            editPane.setPrefWidth(304);
        if(column.equals("mobile"))
            editPane.setPrefWidth(294);
        if(column.equals("address"))
            editPane.setPrefWidth(288);
        editPane.setAlignment(Pos.CENTER_LEFT);
        namePane.setHgap(40);
        namePane.setTranslateX(300);
        namePane.setTranslateY(yTranslate);
        return namePane;
    }

    private GridPane customerOrders(GridPane menuPane, GridPane displayPane, int yTranslate, String menuOption){
        orderPane = new GridPane();
        orderPane.prefWidthProperty().bind(menuPane.widthProperty());
        orderPane.setPrefHeight(40);
        orderPane.setTranslateY(yTranslate);
        Text text = new Text("Orders");
        orderPane.getChildren().add(text);
        orderPane.setAlignment(Pos.CENTER);
        if(menuOption.equals("order")){
            orderPane.setStyle("-fx-background-color: beige");
            displayPane.getChildren().clear();
            displayPane.getChildren().add(printOrders(displayPane));
        }
        orderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){
                orderPane.setStyle("-fx-background-color: beige");
                profilePane.setStyle("-fx-background-color: white");
                cartPane.setStyle("-fx-background-color: white");
                privacyPane.setStyle("-fx-background-color: white");
                displayPane.getChildren().clear();
                displayPane.getChildren().add(printOrders(displayPane));
            }
        });
        return orderPane;
    }

    private GridPane printOrders(GridPane displayPane){
        GridPane gp = new GridPane();
        gp.getChildren().add(OrderList.getAllOrders(customer));
        return gp;
    }

    private GridPane customerCart(GridPane menuPane, GridPane displayPane, int yTranslate, String menuOption){
        cartPane = new GridPane();
        cartPane.prefWidthProperty().bind(menuPane.widthProperty());
        cartPane.setPrefHeight(40);
        cartPane.setTranslateY(yTranslate);
        Text text = new Text("Cart");
        cartPane.getChildren().add(text);
        cartPane.setAlignment(Pos.CENTER);
        if(menuOption.equals("cart")){
            cartPane.setStyle("-fx-background-color: beige");
            displayPane.getChildren().clear();
            displayPane.getChildren().add(printCart(displayPane));
        }
        cartPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){
                cartPane.setStyle("-fx-background-color: beige");
                profilePane.setStyle("-fx-background-color: white");
                orderPane.setStyle("-fx-background-color: white");
                privacyPane.setStyle("-fx-background-color: white");
                displayPane.getChildren().clear();
                displayPane.getChildren().add(printCart(displayPane));
            }
        });
        return cartPane;
    }

    private GridPane printCart(GridPane displayPane){
        GridPane gp = new GridPane();
        gp.getChildren().add(CartList.getAllCart(customer, "default"));
        return gp;
    }

    private GridPane customerPrivacy(GridPane menuPane, GridPane displayPane, int yTranslate){
        privacyPane = new GridPane();
        privacyPane.prefWidthProperty().bind(menuPane.widthProperty());
        privacyPane.setPrefHeight(40);
        privacyPane.setTranslateY(yTranslate);
        Text text = new Text("Privacy");
        privacyPane.getChildren().add(text);
        privacyPane.setAlignment(Pos.CENTER);

        privacyPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){
                privacyPane.setStyle("-fx-background-color: beige");
                profilePane.setStyle("-fx-background-color: white");
                orderPane.setStyle("-fx-background-color: white");
                cartPane.setStyle("-fx-background-color: white");
                displayPane.getChildren().clear();
                displayPane.getChildren().add(printPrivacySetting(displayPane));
            }
        });
        return privacyPane;
    }

    private GridPane printPrivacySetting(GridPane displayPane){
        GridPane privacyGridPane = new GridPane();
        GridPane passwordPane = new GridPane();
        Button changePasswordButton = new Button("Change password");
        passwordPane.getChildren().add(changePasswordButton);
        changePasswordButton.prefWidthProperty().bind(passwordPane.widthProperty());
        GridPane deleteAccountPane = new GridPane();
        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountPane.getChildren().add(deleteAccountButton);
        deleteAccountButton.prefWidthProperty().bind(deleteAccountPane.widthProperty());
        passwordPane.setPrefWidth(200);

        privacyGridPane.add(passwordPane, 0, 0);
        privacyGridPane.add(deleteAccountPane, 0, 1);

        privacyGridPane.setVgap(50);

        privacyGridPane.setTranslateY(200);
        privacyGridPane.setTranslateX(400);

        Label currentPassLabel = new Label("Current Password");
        PasswordField currentPassField = new PasswordField();
        Label newPassLabel = new Label("New Password");
        PasswordField newPassField = new PasswordField();
        PasswordField reNewPassField = new PasswordField();
        currentPassField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currentPassField.setStyle("-fx-border-color: transparent");
            }
        });
        newPassField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newPassField.setStyle("-fx-border-color: transparent");
            }
        });
        reNewPassField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reNewPassField.setStyle("-fx-border-color: transparent");
            }
        });

        Button changeButton = new Button("Change");

        changeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String currPass = currentPassField.getText();
                String newPass = newPassField.getText();
                String rePass = reNewPassField.getText();
                if(currPass.isEmpty())
                    currentPassField.setStyle("-fx-border-color: red");
                else if(!customer.getPassword().equals(Login.getEncryptedPassword(currPass))) {
                    ECommerce.showDialogue("Incorrect Current Password");
                    currentPassField.setStyle("-fx-border-color: red");
                }
                else if(newPass.isEmpty())
                    newPassField.setStyle("-fx-border-color: red");
                else if(rePass.isEmpty())
                    reNewPassField.setStyle("-fx-border-color: red");
                else if(!newPass.equals(rePass))
                    ECommerce.showDialogue("New Passwords do not match");
                else if(newPass.equals(currPass))
                    ECommerce.showDialogue("New password cannot be previous password");
                else if(newPass.equals(rePass)){
                    boolean changeStatus = false;
                    changeStatus = Login.changePassword(customer.getId(), newPass);
                    if(changeStatus) {
                        ECommerce.showDialogue("Password change successful");
                        privacyGridPane.getChildren().clear();
                        currentPassField.setText("");
                        newPassField.setText("");
                        reNewPassField.setText("");
                        privacyGridPane.add(passwordPane, 0, 0);
                        privacyGridPane.add(deleteAccountPane, 0, 1);
                        customer.setPassword(Login.getEncryptedPassword(newPass));
                        privacyGridPane.setVgap(50);
                    }
                    else
                        ECommerce.showDialogue("Password change failed");
                }
            }
        });

        Button deleteButton = new Button("DELETE ACCOUNT");

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String currPass = Login.getEncryptedPassword(currentPassField.getText());
                if(!customer.getPassword().equals(currPass))
                    ECommerce.showDialogue("Incorrect Password");
                if(currPass.equals(customer.getPassword())){
                    boolean deleteStatus = false;
                    deleteStatus = Login.deleteAccount(customer.getId());
                    if(deleteStatus){
                        ECommerce.showDialogue("Account Deleted");
                        privacyGridPane.getChildren().clear();
                        privacyGridPane.add(passwordPane, 0, 0);
                        privacyGridPane.add(deleteAccountPane, 0, 1);
                        currentPassField.setText("");
                        privacyGridPane.setVgap(50);
                        privacyGridPane.setTranslateY(200);
                        privacyGridPane.setTranslateX(400);
                    }
                    else
                        ECommerce.showDialogue("Failed to Delete");
                }
            }
        });

        changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                privacyGridPane.getChildren().clear();
                privacyGridPane.add(currentPassLabel, 0, 0);
                privacyGridPane.add(currentPassField, 1, 0);
                privacyGridPane.add(newPassLabel, 0, 1);
                privacyGridPane.add(newPassField, 1, 1);
                privacyGridPane.add(reNewPassField, 1, 2);
                privacyGridPane.getChildren().add(changeButton);
                changeButton.setTranslateY(150);
                changeButton.setTranslateX(150);
                privacyGridPane.setVgap(20);
                privacyGridPane.setHgap(10);
            }
        });

        deleteAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                privacyGridPane.getChildren().clear();
                privacyGridPane.setTranslateX(350);
                privacyGridPane.add(currentPassLabel, 0, 0);
                privacyGridPane.add(currentPassField, 1, 0);
                privacyGridPane.add(deleteButton, 2, 0);
                privacyGridPane.setHgap(10);
            }
        });

        return privacyGridPane;
    }

}

