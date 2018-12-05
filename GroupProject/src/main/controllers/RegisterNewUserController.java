package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.DatabaseUtility;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.zip.ZipEntry;

public class RegisterNewUserController {
    private ControllerUtility utility = new ControllerUtility();

    @FXML
    private Button cancel;
    @FXML
    private Button submit;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
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
    public void submitNewUser() throws IOException, SQLException {
        String userName = username.getText();
        String pw = password.getText();
        String fn = firstName.getText();
        String ln = lastName.getText();
        String em = email.getText();
        String cntry = country.getText();
        String streetnum = streetNumber.getText();
        String unitnum = unitNumber.getText();
        String street = streetName.getText();
        String cityname = city.getText();
        String st = state.getText();
        String zp = zip.getText();
        String ccnum = creditCardNumber.getText();
        String pcntry = paymentCountry.getText();
        String pstreetnum = paymentStreetNumber.getText();
        String punit = paymentUnitNumber.getText();
        String pstreet = paymentStreetName.getText();
        String pcity = paymentCity.getText();
        String pstate = paymentState.getText();
        String pzip = paymentZip.getText();
        String cardtype = cardType.getText();
        String pmo = cardExpirationMonth.getText();
        String pyr = cardExpirationYear.getText();

        if( username.getText().equals("") || password.getText().equals("")){
            utility.showAlert("Registration Error", "You must enter a username and password!");
        }
        DatabaseUtility dbUtil = new DatabaseUtility();
        String query = "SELECT * FROM User WHERE UserName=\'"+username.getText()+"\';";
        ResultSet getUser = dbUtil.queryDatabase(query);
        String createUser = "INSERT INTO User (FirstName, LastName, isAdmin, UserName, Password, Email) VALUES (\'"+firstName.getText()+"\', \'"+lastName.getText()+"\', 'F', \'"+username.getText()+"\', \'"+password.getText()+"\', \'"+ email.getText()+"\');";
        String payment = "INSERT INTO User (FirstName, LastName, isAdmn, UserName, Password) VALUES ('test1', 'test1', 'testStreet', 'tesyCity', 1112223333, 'F', 'user', 'password');";
        if (getUser.next()) {
            utility.showAlert("Registration Error", "That username already exists!");
        } else {
            dbUtil.insertDatabase(createUser);
            getUser = dbUtil.queryDatabase(query);
            getUser.next();
            String[] fields = new String[]{"UserID", "Country", "StreetNumber", "Unit", "StreetName", "City", "State", "ZipCode"};
            String[] values = new String[]{getUser.getString(1), "\'"+country.getText()+"\'",streetNumber.getText(),unitNumber.getText(), "\'"+streetName.getText()+"\'", "\'"+city.getText()+"\'", "\'"+state.getText()+"\'", zip.getText()};
            dbUtil.insert("Address", fields, values);
            query = "SELECT * FROM Address WHERE UserID="+getUser.getString(1)+";";
            ResultSet getAddr = dbUtil.queryDatabase(query);
            getAddr.next();
            fields = new String[] {"UserID", "AddressID", "CartType", "CardNumber", "CardExpMonth", "CardExpYear"};
            // TODO: HANDLE CASE WHERE USER ENTER DIFFERENT ADDRESS INFO STILL
            values = new String[] {getUser.getString(1), getAddr.getString(1), "\'"+cardType.getText()+"\'", creditCardNumber.getText(), cardExpirationMonth.getText(), cardExpirationYear.getText(),};
            dbUtil.insert("PaymentInfo", fields, values);
            utility.back();
        }
    }

    @FXML
    public void back() throws IOException {
        utility.back();
    }
}
