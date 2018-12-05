package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import main.DatabaseUtility;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

public class PlaceOrderController {

    private final ObservableList<Integer> quantities =
            FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
    @FXML
    private Button placeOrder;
    @FXML
    private Button back;
    @FXML
    private Hyperlink signOut;
    @FXML
    private ComboBox vanillaQuant;
    @FXML
    private ComboBox chocolateQuant;
    @FXML
    private ComboBox strawberryQuant;



    private ControllerUtility utility = new ControllerUtility();
    private DatabaseUtility dbUtil = new DatabaseUtility();


    @FXML
    void initialize(){
        vanillaQuant.getItems().clear();
        chocolateQuant.getItems().clear();
        strawberryQuant.getItems().clear();
        vanillaQuant.setItems(quantities);
        chocolateQuant.setItems(quantities);
        strawberryQuant.setItems(quantities);
        vanillaQuant.getSelectionModel().selectFirst();
        chocolateQuant.getSelectionModel().selectFirst();
        strawberryQuant.getSelectionModel().selectFirst();

    }

    @FXML
    public void placeOrderPressed() throws IOException, SQLException {
        int vanillaQuant = (int) this.vanillaQuant.getValue();
        int chocolateQuant = (int) this.chocolateQuant.getValue();
        int strawberry = (int) this.strawberryQuant.getValue();
        int uid = dbUtil.getCurrentUser().getUserID();
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
        String[] fields = new String[]{"UserID", "Status", "OrderDate"};
        String[] values = new String[]{"\'"+String.valueOf(uid)+"\'", "\'Order Placed\'", "\'"+currentTime+"\'"};
        dbUtil.insert("Orders", fields, values);

        String query = "SELECT * FROM Orders ORDER BY OrderID DESC LIMIT 1";
        ResultSet result = dbUtil.queryDatabase(query);
        result.next();
        int orderID = result.getInt(1);

        if(vanillaQuant != 0){
            query = "SELECT * FROM Product WHERE Flavor=\'Vanilla\'";
            result = dbUtil.queryDatabase(query);
            result.next();
            int currentQuant = result.getInt(3);
            if(currentQuant < vanillaQuant){
                utility.showAlert("Order Error", "We don't have enough Vanilla Ice Cream, please try again later.");
            } else {
                fields = new String[]{"ProductID", "Quantity", "OrderID"};
                values = new String[]{"\'0\'", "\'"+String.valueOf(vanillaQuant)+"\'", "\'"+String.valueOf(orderID)+"\'"};
                dbUtil.insert("OrderItems", fields, values);
                fields = new String[]{"QuantityInInventory"};
                values = new String[]{"\'"+String.valueOf(currentQuant-vanillaQuant)+"\'"};


                dbUtil.update("Product", fields, values, "Flavor", "\'Vanilla\'");
            }
        }
        if(chocolateQuant != 0){
            query = "SELECT * FROM Product WHERE Flavor=\'Chocolate\'";
            result = dbUtil.queryDatabase(query);
            result.next();
            int currentQuant = result.getInt(3);
            if(currentQuant < chocolateQuant){
                utility.showAlert("Order Error", "We don't have enough Chocolate Ice Cream, please try again later.");
            } else {
                fields = new String[]{"ProductID", "Quantity", "OrderID"};
                values = new String[]{"\'1\'", "\'"+String.valueOf(chocolateQuant)+"\'", "\'"+String.valueOf(orderID)+"\'"};
                dbUtil.insert("OrderItems", fields, values);
                fields = new String[]{"QuantityInInventory"};
                values = new String[]{"\'"+String.valueOf(currentQuant-chocolateQuant)+"\'"};
                dbUtil.update("Product", fields, values, "Flavor", "\'Chocolate\'");
            }
        }
        if(strawberry != 0){
            query = "SELECT * FROM Product WHERE Flavor=\'Strawberry\'";
            result = dbUtil.queryDatabase(query);
            result.next();
            int currentQuant = result.getInt(3);
            if(chocolateQuant < strawberry){
                utility.showAlert("Order Error", "We don't have enough Strawberry Ice Cream, please try again later.");
            } else {
                fields = new String[]{"ProductID", "Quantity", "OrderID"};
                values = new String[]{"\'2\'", "\'"+String.valueOf(strawberry)+"\'", "\'"+String.valueOf(orderID)+"\'"};
                dbUtil.insert("OrderItems", fields, values);
                fields = new String[]{"QuantityInInventory"};
                values = new String[]{"\'"+String.valueOf(currentQuant-strawberry)+"\'"};
                dbUtil.update("Product", fields, values, "Flavor", "\'Strawberry\'");
            }
        }
        utility.showAlert("Order Confirmation", "Your order has been successfully placed!");
        Stage stage = (Stage) placeOrder.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/user_landing_page.fxml");
    }

    @FXML
    public void signOutPressed() throws IOException {
        Stage stage = (Stage) signOut.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/login.fxml");
    }

    @FXML
    public void backPressed() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        utility.back();
    }
}
