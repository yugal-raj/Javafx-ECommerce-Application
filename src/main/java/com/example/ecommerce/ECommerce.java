package com.example.ecommerce;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;

public class ECommerce extends Application {
    static BorderPane root;
    private static Text pageNo;
    static ProductList productList = new ProductList();
//    Product product = null;
    static GridPane bodyPane;
    private static int totalPage;
    public static int page = 1;
    private static String search;
    static Button signInButton = new Button("Log In");
    static Label welcomeLabel = new Label("Welcome Customer");


    static Customer loggedInCustomer = null;
    static void getPage(int listSize){
        totalPage = (int) Math.ceil(listSize/6.0);
    }

    static GridPane loginPage(){
        Label userLabel = new Label("User Name");
        Label passLabel = new Label("Password");
//        userLabel.setStyle("-fx-font: normal bold 20px 'serif' ");
        TextField userName = new TextField();
        userName.setPromptText("Enter email");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        Button loginButton = new Button("Login");
        Label messageLabel = new Label("");

        loginButton.setOnAction(actionEvent -> {
            String user = userName.getText();
            String pass = password.getText();
            loggedInCustomer = Login.customerLogin(user, pass);
            if(loggedInCustomer != null) {
                welcomeLabel.setText("Welcome " + loggedInCustomer.getName());
                bodyPane.getChildren().clear();
                page = 1;
                bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, "", page));
                root.setTop(headerBar());
                root.setBottom(footerBar());
            }
            else {
                showDialogue("Incorrect credentials");
            }
        });
        GridPane loginBody = new GridPane();
        GridPane loginPane = new GridPane();
        loginPane.setStyle("-fx-background-color: BEIGE;");
        Text hello = new Text("Hello");
        hello.setStyle("-fx-font: normal bold 50px 'serif' ");
        hello.setTranslateX(80);
        Text signInMessage = new Text("Sign in to ECOMMERCE");
        signInMessage.setTranslateX(75);
        loginPane.add(hello, 0, 0);
        loginPane.add(signInMessage, 0, 1);
        loginPane.setAlignment(Pos.BOTTOM_CENTER);


        loginPane.add(userLabel, 0, 2);
        loginPane.add(userName, 1, 2);
        loginPane.add(passLabel, 0, 3);
        loginPane.add(password, 1, 3);
        userLabel.setTranslateX(25);
        passLabel.setTranslateX(25);
        userName.setTranslateX(-45);
        password.setTranslateX(-45);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setVgap(20);
        loginPane.add(loginButton, 0, 4);
        loginPane.add(messageLabel, 1, 4);

        loginButton.setTranslateX(120);
        messageLabel.setTranslateX(40);
        loginPane.setAlignment(Pos.TOP_CENTER);
        Text newUserMessage = new Text("New to ECommerce?");
        Text signUp = new Text("Create an account");
        signUp.setFill(Color.BLUE);
        loginPane.add(newUserMessage, 0, 5);
        loginPane.add(signUp, 1, 5);
        newUserMessage.setTranslateX(40);
        loginPane.setHgap(30);

        loginPane.prefHeightProperty().bind(bodyPane.heightProperty());

        signUp.setOnMouseClicked(e -> {
            bodyPane.getChildren().clear();
            GridPane signUpPane = NewCustomer.getSignUpPane();
            bodyPane.getChildren().add(signUpPane);
            signUpPane.setPrefWidth(600);
            signUpPane.prefHeightProperty().bind(bodyPane.heightProperty());
            signUpPane.setStyle("-fx-background-color: white");
            signUpPane.setAlignment(Pos.CENTER);
        });
//        loginPane.setPadding(new Insets(10, 10, 10, 10));
        loginBody.getChildren().add(loginPane);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPrefWidth(600);
        loginPane.setStyle("-fx-background-color: white");
        return loginBody;
    }
    static BorderPane headerBar(){
        BorderPane header = new BorderPane();

        TextField searchBar = new TextField();
        searchBar.setPrefWidth(500);
        Button searchButton = new Button("Search");
        header.setStyle("-fx-background-color: BLACK;");
        searchButton.setOnAction(actionEvent -> {
            bodyPane.getChildren().clear();
            search = searchBar.getText();
            page = 1;
            bodyPane.getChildren().addAll(productList.getAllProducts(loggedInCustomer, search, page));
//                bodyPane.setStyle("-fx-background-color: BEIGE;");
            root.setBottom(footerBar());
        });
        Text applicationNameText = new Text("ECOMMERCE");
        Glow glow = new Glow();

        applicationNameText.setOnMouseClicked(mouseEvent -> {
            bodyPane.getChildren().clear();
            page = 1;
            bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, "", page));
            root.setBottom(footerBar());
        });

        glow.setLevel(0.3);
        applicationNameText.setFill(Color.WHITE);
        applicationNameText.setFont(Font.font("Abyssinia SIL",BOLD,REGULAR,20));
        applicationNameText.setEffect(glow);
        applicationNameText.setStroke(Color.YELLOW);
        signInButton.setAlignment(Pos.TOP_RIGHT);

        signInButton.setOnAction(actionEvent -> {
            bodyPane.getChildren().clear();
            bodyPane.getChildren().add(loginPage());
            root.setBottom(null);
        });
        GridPane searchPane = new GridPane();
        GridPane textPane = new GridPane();
        textPane.add(applicationNameText, 0, 0);
        searchPane.add(searchBar, 0, 0);
        searchPane.add(searchButton, 1, 0);

        searchButton.setPrefWidth(100);
        searchButton.setAlignment(Pos.CENTER);

        GridPane leftPane = new GridPane();
        leftPane.add(textPane, 0 , 0);
        leftPane.add(searchPane, 1, 0);

        Button signOutButton = new Button("Sign Out");

        Button cartButton = new Button("Cart");

        GridPane customerPane = new GridPane();
        GridPane welcomeTextPane = new GridPane();
        welcomeTextPane.getChildren().add(welcomeLabel);
        welcomeTextPane.setAlignment(Pos.CENTER);
        customerPane.add(signInButton, 1, 0);
        customerPane.add(welcomeTextPane, 0, 0);
        customerPane.add(cartButton, 2, 0);
        welcomeLabel.setTextFill(Color.WHITE);
        if(loggedInCustomer != null) {
            customerPane.getChildren().remove(signInButton);
            customerPane.add(signOutButton, 1, 0);
        }

        signOutButton.setOnAction(actionEvent -> {
            customerPane.getChildren().remove(signOutButton);
            customerPane.add(signInButton, 1, 0);
            loggedInCustomer = null;
            welcomeLabel.setText("Welcome Customer");
            bodyPane.getChildren().clear();
            page = 1;
            bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, "", page));
            root.setBottom(footerBar());
        });

        CustomerProfile customerProfile = new CustomerProfile();


        cartButton.setOnAction(actionEvent -> {
            if(loggedInCustomer!=null){
                bodyPane.getChildren().clear();
                GridPane profilePane = customerProfile.getCustomerProfile(loggedInCustomer, welcomeLabel, "cart");
                bodyPane.getChildren().add(profilePane);
                profilePane.prefHeightProperty().bind(bodyPane.heightProperty());
                profilePane.prefWidthProperty().bind(bodyPane.widthProperty());
            }
            else
                showDialogue("Please log in");
        });

        header.setLeft(leftPane);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setHgap(20);
        header.setRight(customerPane);
//        searchPane.setAlignment(Pos.CENTER_LEFT);
        textPane.setTranslateX(50);
        searchPane.setTranslateX(160);
        customerPane.setAlignment(Pos.CENTER_RIGHT);
        customerPane.setTranslateX(-30);
        customerPane.setHgap(10);
        header.setPrefHeight(50);

        welcomeLabel.setOnMouseClicked(mouseEvent -> {
            if(loggedInCustomer != null){
                bodyPane.getChildren().clear();
                GridPane profilePane = customerProfile.getCustomerProfile(loggedInCustomer, welcomeLabel, "default");
                bodyPane.getChildren().add(profilePane);
                profilePane.prefHeightProperty().bind(bodyPane.heightProperty());
                profilePane.prefWidthProperty().bind(bodyPane.widthProperty());
            }
        });

        return header;
    }



     static void showDialogue(String message){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Order Status");
        ButtonType type = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        dialog.setContentText(message);

        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }
    static GridPane footerBar(){
        Button decrement = new Button("<");
        Button increment = new Button(">");

        decrement.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        increment.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        GridPane footer = new GridPane();

        decrement.setOnAction(actionEvent -> {
            if(page > 1) {
                page--;
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, search, page));
            }
            pageNo.setText(String.valueOf(page));
        });

        increment.setOnAction(actionEvent -> {
                page++;
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, search, page));
                if(totalPage == 0 && page != 1){
                    page--;
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, search, page));
                }
                pageNo.setText(String.valueOf(page));
        });


        pageNo.setText(String.valueOf(page));

//        footer.setTranslateY(headerLine + height);
        GridPane pagePane = new GridPane();

        pagePane.add(decrement, 0, 0);
        pagePane.add(pageNo,1,0);
        pagePane.add(increment, 2,0);
        footer.getChildren().add(pagePane);
        pagePane.setTranslateY(10);
        pagePane.setHgap(10);
        footer.setPrefHeight(60);
        footer.setAlignment(Pos.CENTER);
        return footer;
    }
    private BorderPane createContent(){
        root = new BorderPane();
        int height = 650;
        int width = 1200;
        root.setPrefSize(width, height);
        bodyPane = new GridPane();
        bodyPane.setPrefSize(width, height);
        search = "";
        pageNo = new Text();
        bodyPane.prefWidthProperty().bind(root.widthProperty());
        bodyPane.getChildren().add(productList.getAllProducts(loggedInCustomer, "", page));
        bodyPane.setAlignment(Pos.CENTER);
        bodyPane.setStyle("-fx-background-color: BEIGE;");
        root.setTop(headerBar());
        root.setCenter(bodyPane);
        root.setBottom(footerBar());
        return root;
    }

    @Override
    public void start(Stage stage) {
        Scene scene;
        scene = new Scene(createContent());
        stage.setTitle("ECommerce");
            stage.setScene(scene);
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}