package main.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button login;
    @FXML
    private Hyperlink registerNewUser;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private ControllerUtility utility = new ControllerUtility();
    private DatabaseUtility dbUtility = new DatabaseUtility();

    @FXML
    public void registerNewUserPressed() throws IOException {
        Stage stage = (Stage) registerNewUser.getScene().getWindow();
        utility.loadNewFXML(stage, "../fxmls/register_new_user.fxml");
    }

    @FXML
    public void loginPressed() throws IOException, SQLException {
        Stage stage = (Stage) login.getScene().getWindow();
        String query = "SELECT * FROM User WHERE UserName=\'"+username.getText()+"\';";
        ResultSet getUser = dbUtility.queryDatabase(query);
        if (!getUser.next()) {
            utility.showAlert("Authentication Error", "That username doesn't exist!");
        }
        else if (!getUser.getString(3).equals(password.getText())){
            utility.showAlert("Authentication Error", "Incorrect password!");
        }
        else if(getUser.getString(8).equals("T")){
            dbUtility.currentUser = new User(getUser.getInt(1), getUser.getString(4), getUser.getString(5), getUser.getString(2), getUser.getString(3), getUser.getString(6), true);
            dbUtility.getCurrentUser().setAddress(getUserAddress(getUser.getInt(1)));
            dbUtility.getCurrentUser().setPaymentMethod(getUserPaymentMethod(getUser.getInt(1)));
            utility.loadNewFXML(stage, "../fxmls/admin_landing_page.fxml");
        }
        else if(getUser.getString(8).equals("F")) {
            dbUtility.currentUser = new User(getUser.getInt(1), getUser.getString(4), getUser.getString(5), getUser.getString(2), getUser.getString(3), getUser.getString(6), false);
            dbUtility.getCurrentUser().setAddress(getUserAddress(getUser.getInt(1)));
            dbUtility.getCurrentUser().setPaymentMethod(getUserPaymentMethod(getUser.getInt(1)));
            utility.loadNewFXML(stage, "../fxmls/user_landing_page.fxml");
        } else {
            utility.showAlert("Authentication Error", "Please enter a valid username and password");
        }
    }

    private Address getUserAddress(int userID) throws SQLException {
        Address address = new Address();
        String query = "SELECT * FROM Address WHERE UserID="+String.valueOf(userID)+";";
        ResultSet result = dbUtility.queryDatabase(query);
        result.next();
        address.setCountry(result.getString(3));
        address.setStreetNumber(result.getInt(4));
        address.setUnitNumber(result.getInt(5));
        address.setStreetName(result.getString(6));
        address.setCity(result.getString(7));
        address.setState(result.getString(8));
        address.setZipCode(result.getInt(9));
        return address;
    }

    private PaymentMethod getUserPaymentMethod(int userID) throws SQLException {
        PaymentMethod paymentMethod = new PaymentMethod();
        String query = "SELECT * FROM PaymentInfo WHERE UserID="+String.valueOf(userID)+";";
        ResultSet result = dbUtility.queryDatabase(query);
        result.next();
        paymentMethod.setTypeOfCard(result.getString(4));
        paymentMethod.setCardNumber(result.getInt(5));
        paymentMethod.setExpMonth(result.getInt(6));
        paymentMethod.setExpYear(result.getInt(7));
        return paymentMethod;
    }

}

