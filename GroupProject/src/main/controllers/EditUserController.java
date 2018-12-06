package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.DatabaseUtility;
import main.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditUserController {

    private ControllerUtility utility = new ControllerUtility();
    private DatabaseUtility dbUtility = new DatabaseUtility();

    @FXML
    private Button cancel;
    @FXML
    private Button submitUpdate;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
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

    public void initialize(){
        User currentUser = dbUtility.getCurrentUser();
        username.setText(currentUser.getUserName());
        password.setText(currentUser.getPassword());
        firstName.setText(currentUser.getFirsName());
        lastName.setText(currentUser.getLastName());
        emailAddress.setText(currentUser.getEmail());
        country.setText(currentUser.getAddress().getCountry());
        streetName.setText(currentUser.getAddress().getStreetName());
        streetNumber.setText(String.valueOf(currentUser.getAddress().getStreetNumber()));
        city.setText(currentUser.getAddress().getCity());
        state.setText(currentUser.getAddress().getState());
        zip.setText(String.valueOf(currentUser.getAddress().getZipCode()));
    }

    @FXML
    public void submitUpdateUser() throws IOException, SQLException {
        // Currently all fields must be filled out (or same as shipping checked for payment address
        //TODO: Allow for empty fields
        if( username.getText().equals("") || password.getText().equals("")){
            utility.showAlert("Update Error", "You must enter a username and password!");
        }
        DatabaseUtility dbUtil = new DatabaseUtility();
        String query = "SELECT * FROM User WHERE Username=\'"+username.getText()+"\';";
        ResultSet getUser = dbUtil.queryDatabase(query);
        String updateUser = "UPDATE User SET FirstName = \'"+firstName.getText()+"\', LastName = \'"+lastName.getText()+"\',  UserName = \'"+username.getText()+"\', Password = \'"+password.getText()+"\', Email = \'"+emailAddress.getText()+"\' WHERE UserID = \'"+dbUtil.getCurrentUser().getUserID()+"\'";
        String payment = "INSERT INTO PaymentInfo (UserID, AddressID, Street, City, Phone, isAdmn, UserName, Password) VALUES ('test1', 'test1', 'testStreet', 'tesyCity', 1112223333, 'F', 'user', 'password');";
        getUser.next();
        String currentUN = dbUtility.getCurrentUser().getUserName();
        String queryUN = getUser.getString(2);
        boolean test = (!currentUN.equals(queryUN));
        if (test) {
            utility.showAlert("Update Error", "That username already exists!");
        } else {
            dbUtil.insertDatabase(updateUser);
            dbUtil.getCurrentUser().setUserName(username.getText());
            dbUtil.getCurrentUser().setFirstName(firstName.getText());
            dbUtil.getCurrentUser().setLastName(lastName.getText());
            dbUtil.getCurrentUser().setEmail(emailAddress.getText());

            String[] fields = new String[] {"UserID", "Country", "StreetNumber", "Unit", "StreetName", "City", "State", "ZipCode"};
            String[] values = new String[] {String.valueOf(dbUtil.getCurrentUser().getUserID()), "\'"+country.getText()+"\'", streetNumber.getText(), unitNumber.getText(), "\'"+streetName.getText()+"\'", "\'"+city.getText()+"\'", "\'"+state.getText()+"\'", zip.getText()};
            dbUtil.insert("Address", fields, values);
            query = "SELECT * FROM Address WHERE UserID="+dbUtil.getCurrentUser().getUserID()+";";
            ResultSet results = dbUtil.queryDatabase(query);
            results.last();
            int addressID;
            if(sameAsShipping.isSelected()){
                addressID = results.getInt(1);
            } else {
                fields = new String[] {"Country", "StreetNumber", "Unit", "StreetName", "City", "State", "ZipCode"};
                values = new String[] {"\'"+paymentCountry.getText()+"\'", paymentStreetNumber.getText(), paymentUnitNumber.getText(), "\'"+paymentStreetName.getText()+"\'", "\'"+paymentCity.getText()+"\'", "\'"+paymentState.getText()+"\'", paymentZip.getText()};
                dbUtil.insert("Address", fields, values);
                query = "SELECT * FROM Address;";
                results = dbUtil.queryDatabase(query);
                results.last();
                addressID = results.getInt(1);
            }
            fields = new String[] {"UserID", "AddressID", "CartType", "CardNumber", "CardExpMonth", "CardExpYear"};
            values = new String[] {String.valueOf(dbUtil.getCurrentUser().getUserID()), String.valueOf(addressID), "\'"+cardType.getText()+"\'", creditCardNumber.getText(), cardExpirationMonth.getText(), cardExpirationYear.getText()};
            dbUtil.update("PaymentInfo", fields, values, "UserID", String.valueOf(dbUtil.getCurrentUser().getUserID()));
            utility.showAlert("Update Accepted", "Update Successful!!");
            utility.back();
        }
    }

    @FXML
    public void cancel() throws IOException {
        Stage stage = (Stage) cancel.getScene().getWindow();
        utility.back();
    }
}
