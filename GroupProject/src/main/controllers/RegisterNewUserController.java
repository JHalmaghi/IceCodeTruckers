package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.controllers.ControllerUtility;

import java.io.IOException;

public class RegisterNewUserController {
    private ControllerUtility utility = new ControllerUtility();

    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    @FXML
    private TextField username;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField emailAddress;
    @FXML
    private TextField country;
    @FXML
    private TextField streetNumber;
    @FXML
    private TextField unitNumber;
    @FXML
    private TextField streetName;
    @FXML
    private TextField city;
    @FXML
    private TextField state;
    @FXML
    private TextField zip;
    @FXML
    private TextField creditCardNumber;
    @FXML
    private TextField paymentCountry;
    @FXML
    private TextField paymentStreetNumber;
    @FXML
    private TextField paymentUnitNumber;
    @FXML
    private TextField paymentStreetName;
    @FXML
    private TextField paymentCity;
    @FXML
    private TextField paymentState;
    @FXML
    private TextField paymentZip;
    @FXML
    private MenuButton cardType;
    @FXML
    private MenuButton cardExpirationMonth;
    @FXML
    private MenuButton cardExpirationYear;
    @FXML
    private CheckBox sameAsShipping;

    @FXML
    public void submitNewUser() throws IOException {
        //add user logic
        utility.back();
    }

    @FXML
    public void back() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        utility.back();
    }
}
