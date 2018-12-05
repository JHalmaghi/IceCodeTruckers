package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.DatabaseUtility;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestockInventoryController {

    ControllerUtility utility = new ControllerUtility();

    private final ObservableList<Integer> quantities =
            FXCollections.observableArrayList(0,10,25,50,75,100,125,150);
    @FXML
    private Button cancel;
    @FXML
    private Button submitOrder;
    @FXML
    private ComboBox vanilla;
    @FXML
    private ComboBox chocolate;
    @FXML
    private ComboBox strawberry;
    @FXML
    private MenuButton restockItem =new MenuButton();

    @FXML
    private TextField quantity;

    private DatabaseUtility dbUtil = new DatabaseUtility();

    @FXML
    void initialize(){
        vanilla.getItems().clear();
        chocolate.getItems().clear();
        strawberry.getItems().clear();
        vanilla.setItems(quantities);
        chocolate.setItems(quantities);
        strawberry.setItems(quantities);
        vanilla.getSelectionModel().selectFirst();
        chocolate.getSelectionModel().selectFirst();
        strawberry.getSelectionModel().selectFirst();

    }

    @FXML
    public void cancelPressed() throws IOException {
        utility.back();
    }

    @FXML
    public void submitPressed() throws IOException, SQLException {
        int vanillaQuant = (int) this.vanilla.getValue();
        int chocolateQuant = (int) this.chocolate.getValue();
        int strawberry = (int) this.strawberry.getValue();
        String query = "SELECT * FROM Product";
        ResultSet result = dbUtil.queryDatabase(query);
        result.absolute(1);
        int currentVanillaQuant = result.getInt(3);
        result.updateInt(3, currentVanillaQuant+vanillaQuant);
        result.updateRow();
        result.next();
        int currentCbocolateQuant = result.getInt(3);
        result.updateInt(3, chocolateQuant+currentCbocolateQuant);
        result.updateRow();
        result.next();
        int currentStrawberryQuant = result.getInt(3);
        result.updateInt(3, strawberry+currentStrawberryQuant);
        result.updateRow();
        utility.showAlert("Order Confirmation", "Your order has been successfully placed!");
        Stage stage = (Stage) submitOrder.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/confirmation.fxml");
    }
}
