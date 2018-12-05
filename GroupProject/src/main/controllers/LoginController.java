package main.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Address;
import main.DatabaseUtility;
import main.Main;
import main.User;

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
            utility.loadNewFXML(stage, "../fxmls/admin_landing_page.fxml");
        }
        else if(getUser.getString(8).equals("F")) {
            dbUtility.currentUser = new User(getUser.getInt(1), getUser.getString(4), getUser.getString(5), getUser.getString(2), getUser.getString(3), getUser.getString(6), false);
            utility.loadNewFXML(stage, "../fxmls/user_landing_page.fxml");
        } else {
            utility.showAlert("Authentication Error", "Please enter a valid username and password");
        }
    }

}

