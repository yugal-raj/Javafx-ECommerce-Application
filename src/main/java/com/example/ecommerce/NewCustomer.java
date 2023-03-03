package com.example.ecommerce;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static javafx.application.Application.launch;

public class NewCustomer {
    public static GridPane getSignUpPane(){
        GridPane signUpPane = new GridPane();

        Text welcomeText = new Text("Create an Account");
        signUpPane.add(welcomeText, 0, 0);
        welcomeText.setTranslateY(-40);
        welcomeText.setTranslateX(70);
        welcomeText.setStyle("-fx-font: normal bold 30px 'san-serif'");

        Label firstNameLabel = new Label("First Name");
        signUpPane.add(firstNameLabel, 0, 1);
        TextField nameField = new TextField();
        nameField.setPromptText("Enter first name");
        signUpPane.add(nameField, 1, 1);
        nameField.setTranslateX(-80);
        firstNameLabel.setTranslateX(70);
        nameField.setOnMouseClicked(mouseEvent -> nameField.setStyle("-fx-border-color: transparent"));

        Label lastNameLabel = new Label("Last Name");
        signUpPane.add(lastNameLabel, 0, 2);
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        signUpPane.add(lastNameField, 1, 2);
        lastNameField.setTranslateX(-80);
        lastNameLabel.setTranslateX(70);
        lastNameField.setOnMouseClicked(mouseEvent -> lastNameField.setStyle("-fx-border-color: transparent"));

        Label emailLabel = new Label("Email");
        signUpPane.add(emailLabel, 0, 3);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");
        signUpPane.add(emailField, 1, 3);
        emailField.setTranslateX(-80);
        emailLabel.setTranslateX(70);
        emailField.setOnMouseClicked(mouseEvent -> emailField.setStyle("-fx-border-color: transparent"));


        Label addressLabel = new Label("Address");
        signUpPane.add(addressLabel, 0, 4);
        TextField addressField = new TextField();
        addressField.setPromptText("Enter address");
        signUpPane.add(addressField, 1, 4);
        addressField.setTranslateX(-80);
        addressLabel.setTranslateX(70);
        addressField.setOnMouseClicked(mouseEvent -> addressField.setStyle("-fx-border-color: transparent"));

        Label mobileLabel = new Label("Mobile");
        signUpPane.add(mobileLabel, 0, 5);
        TextField mobileField = new TextField();
        mobileField.setPromptText("Enter mobile number");
        signUpPane.add(mobileField, 1, 5);
        mobileField.setTranslateX(-80);
        mobileLabel.setTranslateX(70);
        mobileField.setOnMouseClicked(mouseEvent -> mobileField.setStyle("-fx-border-color: transparent"));

        Label messageLabel = new Label();
        signUpPane.add(messageLabel, 0, 9);
        messageLabel.setTranslateY(20);
        messageLabel.setTranslateX(150);

        Label passwordLabel = new Label("Password");
        signUpPane.add(passwordLabel, 0, 6);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        signUpPane.add(passwordField, 1, 6);
        PasswordField rePasswordField = new PasswordField();
        rePasswordField.setPromptText("Re-enter password");
        signUpPane.add(rePasswordField, 1, 7);
        passwordField.setTranslateX(-80);
        rePasswordField.setTranslateX(-80);
        passwordLabel.setTranslateX(70);
        passwordField.setOnMouseClicked(mouseEvent -> {
            passwordField.setStyle("-fx-border-color: transparent");
            messageLabel.setText(null);
        });
        rePasswordField.setOnMouseClicked(mouseEvent -> {
            rePasswordField.setStyle("-fx-border-color: transparent");
            messageLabel.setText(null);
        });

        Button registerButton = new Button("Register");
        signUpPane.add(registerButton, 0, 8);
        registerButton.setTranslateX(160);
        registerButton.setPrefWidth(100);
        registerButton.setTranslateY(20);


//        messageLabel.setTextFill(Color.RED);

        registerButton.setOnAction(actionEvent -> {
            String firstName, lastName, address, email, mobile, password, rePassword;
            firstName = nameField.getText();
            lastName = lastNameField.getText();
            address = addressField.getText();
            email = emailField.getText();
            mobile = mobileField.getText();
            password = passwordField.getText();
            rePassword = rePasswordField.getText();
            boolean complete = true;
            if(nameField.getText().isEmpty()) {
                nameField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(lastNameField.getText().isEmpty()) {
                lastNameField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(emailField.getText().isEmpty()) {
                emailField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(addressField.getText().isEmpty()) {
                addressField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(mobileField.getText().isEmpty()) {
                mobileField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(passwordField.getText().isEmpty()) {
                passwordField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(rePasswordField.getText().isEmpty()) {
                rePasswordField.setStyle("-fx-border-color: red");
                complete = false;
            }
            else if(!passwordField.getText().isEmpty() && !rePasswordField.getText().isEmpty()) {
                if(!password.equals(rePassword)) {
                    messageLabel.setText("Passwords do not match");
                    messageLabel.setTextFill(Color.RED);
                    complete = false;
                }
            }
            if(complete){
                boolean registerStatus = Login.newCustomerLogin(firstName+" "+lastName, email, address, mobile, password);
                if(registerStatus) {
                    Customer customer = Login.customerLogin(email, password);
                    //CREATE TABLE `ecomm`.`cart2` (`cid` INT NOT NULL AUTO_INCREMENT,`productId` INT NOT NULL, `quantity` INT NOT NULL, PRIMARY KEY (`cid`))
                    assert customer != null;
                    Login.newCart(customer.getId());
                    ECommerce.welcomeLabel.setText("Welcome " + customer.getName());
                    ECommerce.bodyPane.getChildren().clear();
                    ECommerce.page = 1;
                    ECommerce.loggedInCustomer = customer;
                    ECommerce.bodyPane.getChildren().add(ECommerce.productList.getAllProducts(ECommerce.loggedInCustomer, "", ECommerce.page));
                    ECommerce.root.setTop(ECommerce.headerBar());
                    ECommerce.root.setBottom(ECommerce.footerBar());
                }
                else
                    ECommerce.showDialogue("Registration Failed\nEmail or Mobile linked to another account");
            }
        });

        GridPane registeredUserPane = new GridPane();
        Text registeredUserMessage = new Text("Already have an account?");
        Text logInText = new Text("Log in");

        logInText.setOnMouseClicked(e->{
            ECommerce.bodyPane.getChildren().clear();
            ECommerce.bodyPane.getChildren().add(ECommerce.loginPage());
        });

        logInText.setFill(Color.BLUE);
        registeredUserPane.add(registeredUserMessage, 0, 0);
        registeredUserPane.add(logInText, 1, 0);
        signUpPane.add(registeredUserPane, 0, 9);
        registeredUserPane.setHgap(10);
        registeredUserPane.setTranslateY(30);
        registeredUserPane.setTranslateX(120);


        signUpPane.setVgap(5);
        return signUpPane;
    }
}
