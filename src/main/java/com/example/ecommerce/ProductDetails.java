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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ProductDetails {
   private static Product product;
   static int quantity;
    public static TableView<Product> productTable;

   public static BorderPane getDetails(Product product, Customer loggedInCustomer){
        ProductDetails.product = product;
        quantity = 1;

        BorderPane productDetails = new BorderPane();
        Text name = new Text(product.getName());
        Text price = new Text(String.valueOf(product.getPrice()));
        name.setFont(Font.font(20));
        price.setFont(Font.font(16));
        Button buyButton = new Button("Buy Now");
        GridPane details = new GridPane();

        InputStream stream = null;

        try {
            stream = new FileInputStream(product.getImageLocation());
        }
        catch (FileNotFoundException e) {
           throw new RuntimeException(e);
        }

        Image image = new Image(stream);
       //Creating the image view
        ImageView imageView = new ImageView();
       //Setting image to the image view
        imageView.setImage(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(400);

        GridPane imagePane = new GridPane();
        imagePane.getChildren().add(imageView);

        Button addToCartButton = new Button("Add to Cart");
        GridPane quantityPane = new GridPane();
        Button decrementButton = new Button("<");
        Button incrementButton = new Button(">");

        GridPane numberPane = new GridPane();
        Text number = new Text(String.valueOf(quantity));

        decrementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(quantity > 0){
                    quantity--;
                    number.setText(String.valueOf(quantity));
                }
            }
        });

        incrementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(quantity < product.getQuantity()){
                    quantity++;
                    number.setText(String.valueOf(quantity));
                }
            }
        });

        numberPane.getChildren().add(number);
        numberPane.setPrefWidth(40);
        numberPane.setAlignment(Pos.CENTER);
        quantityPane.add(decrementButton, 0, 0);
        quantityPane.add(numberPane, 1, 0);
        quantityPane.add(incrementButton, 2, 0);
        if(product.getQuantity() == 0){
            Text alertText = new Text("out of stock");
            alertText.setFill(Color.RED);
            alertText.setFont(Font.font(30));
            quantityPane.getChildren().clear();
            quantityPane.getChildren().add(alertText);
        }
        GridPane buttonPane = new GridPane();
        buttonPane.add(buyButton, 0, 0);
        buttonPane.add(addToCartButton, 1, 0);

        details.add(name, 0, 0);
        details.add(price, 0, 1);
        details.add(quantityPane, 0, 2);
        details.add(buttonPane, 0, 3);

        Text alertText = new Text();
        GridPane alertPane = new GridPane();
        alertPane.getChildren().add(alertText);
        if(product.getQuantity() <= 50)
            alertText.setText("Only " + String.valueOf(product.getQuantity()) + " in stock");
        alertText.setFill(Color.RED);
        details.getChildren().add(alertPane);
        alertPane.setTranslateY(140);

        quantityPane.setTranslateY(40);
        buttonPane.setTranslateY(90);
        buttonPane.setHgap(30);
        details.setVgap(10);

        ECommerce obj = new ECommerce();

        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(product.getQuantity() == 0)
                    ECommerce.showDialogue("OUT OF STOCK");
                else {
                    boolean orderStatus = false;
                    if (loggedInCustomer != null && quantity > 0 && quantity <= product.getQuantity()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        orderStatus = Order.placeOrder(loggedInCustomer, product, quantity, formatter.format(date));
                    } else if (loggedInCustomer == null)
                        obj.showDialogue("Please log in");
                    else if (quantity == 0)
                        ECommerce.showDialogue("quantity can not be 0");
                    if (orderStatus) {
                        obj.showDialogue("Order Successful");
                        CustomerProfile customerProfile = new CustomerProfile();
                        GridPane profilePane = customerProfile.getCustomerProfile(loggedInCustomer, ECommerce.welcomeLabel, "order");
                        ECommerce.bodyPane.getChildren().clear();
                        ECommerce.bodyPane.getChildren().add(profilePane);
                        profilePane.prefHeightProperty().bind(ECommerce.bodyPane.heightProperty());
                        profilePane.prefWidthProperty().bind(ECommerce.bodyPane.widthProperty());
                    } else
                        ECommerce.showDialogue("Order Failed");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(loggedInCustomer == null){
                    ECommerce.showDialogue("Please Log in");
                }
                else{
                   boolean cartStatus =  Cart.addToCart(loggedInCustomer, product, quantity);
                   if(cartStatus)
                       ECommerce.showDialogue("added to cart");
                   else
                       ECommerce.showDialogue("couldn't add to cart");
                }
            }
        });



        productDetails.setRight(details);
        productDetails.setLeft(imagePane);
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setTranslateX(-100);
        imagePane.setTranslateY(-50);
        imagePane.setStyle("-fx-padding: 10;"+
                            "-fx-border-style: solid inside;"+
                            "-fx-border-color: black;" +
                            "-fx-background-color: white"
                            );
        imagePane.setAlignment(Pos.CENTER);
        return productDetails;
   }
}
