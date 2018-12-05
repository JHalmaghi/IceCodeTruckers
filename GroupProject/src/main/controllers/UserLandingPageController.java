package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.DatabaseUtility;
import main.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserLandingPageController {

    @FXML
    private Button placeNewOrder;
    @FXML
    private Hyperlink signOut;
    @FXML
    private Hyperlink editAccount;
    @FXML
    private VBox prevOrders;
    @FXML
    private Text welcome;

    List<Hyperlink> orderLinks = new ArrayList<>();

    private ControllerUtility utility = new ControllerUtility();
    private DatabaseUtility dbUtil = new DatabaseUtility();

    @FXML
    public void initialize() throws SQLException {
        User currentUser = dbUtil.getCurrentUser();
        welcome.setText("Welcome, "+currentUser.getFirsName()+" "+currentUser.getLastName()+"!");
        String query = "SELECT * FROM Orders WHERE UserID=" +String.valueOf(dbUtil.getCurrentUser().getUserID())+ " ORDER BY OrderID DESC LIMIT 3";
        ResultSet resultSet = dbUtil.queryDatabase(query);
        while(resultSet.next()){
            String orderNumber = resultSet.getString(1);
            Hyperlink currentOrder = new Hyperlink();
            String orderPlaced = resultSet.getString(4);
            String orderStatus = resultSet.getString(3);
            ArrayList<String> flavors = new ArrayList<>();
            ArrayList<Integer> quantityOrdered = new ArrayList<>();
            String orderItemsQuery = "SELECT * FROM OrderItems WHERE OrderID="+resultSet.getString(1)+";";
            ResultSet orderItemsResult = dbUtil.queryDatabase(orderItemsQuery);
            while (orderItemsResult.next()){
                String productQuery = "SELECT * FROM Product WHERE ProductID="+orderItemsResult.getString(2)+";";
                ResultSet productResults = dbUtil.queryDatabase(productQuery);
                productResults.next();
                String prod = productResults.getString(2);
                flavors.add(prod);
                quantityOrdered.add(orderItemsResult.getInt(3));
            }
            currentOrder.setOnAction(event -> {
                String displayMessage = "Order #"+orderNumber+" \n";
                for(int i=0; i<flavors.size(); i++){
                    displayMessage += flavors.get(i)+ ": "+String.valueOf(quantityOrdered.get(i))+"\n";
                }
                displayMessage += "\n Status: "+ orderStatus;
                displayMessage += "\n Date Ordered: "+orderPlaced;
                utility.showAlert("Order Details", displayMessage);
            });
            currentOrder.setText("Order #"+orderNumber);
            orderLinks.add(currentOrder);
        }
        prevOrders.getChildren().clear();
        prevOrders.getChildren().addAll(orderLinks);

    }

    @FXML
    public void placeNewOrderPressed() throws IOException {
        Stage stage = (Stage) placeNewOrder.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/place_order.fxml");
    }

    @FXML
    public void signOutPressed() throws IOException {
        Stage stage = (Stage) signOut.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/login.fxml");
    }

    @FXML
    public void editAccountPressed() throws IOException {
        Stage stage = (Stage) editAccount.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/edit_user.fxml");
    }
}