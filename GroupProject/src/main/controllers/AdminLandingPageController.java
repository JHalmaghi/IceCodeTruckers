package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.DatabaseUtility;
import main.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminLandingPageController {
    ControllerUtility utility = new ControllerUtility();

    @FXML
    private Button restockInventory;

    @FXML
    private Hyperlink editAccount;

    @FXML
    private Hyperlink signOut;

    @FXML
    private Hyperlink currentInventory;

    @FXML
    private Text welcome;

    DatabaseUtility dbUtil = new DatabaseUtility();

    @FXML
    public void initialize(){
        User currentUser = dbUtil.getCurrentUser();
        welcome.setText("Welcome, "+currentUser.getFirsName()+" "+currentUser.getLastName()+"!");
    }
    @FXML
    public void restockInventoryPressed() throws IOException {
        Stage stage = (Stage) restockInventory.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/restock_inventory.fxml");
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

    @FXML
    public void currentInvPressed() throws SQLException {
        String invQuery = "SELECT * FROM Product;";
        ResultSet invResult = dbUtil.queryDatabase(invQuery);
        ArrayList<String> products = new ArrayList<>();
        ArrayList<Integer> inventory = new ArrayList<>();
        while (invResult.next()){
            String currentProd = invResult.getString(2);
            products.add(currentProd);
            Integer currentInv = invResult.getInt(3);
            inventory.add(currentInv);
        }
        String displayMsg = "CURRENT INVENTORY\n";
        for(int i=0; i<products.size(); i++){
            displayMsg += products.get(i)+ ": "+inventory.get(i)+"\n";
        }
        utility.showAlert("Report Results", displayMsg);
    }
}
