package main;

import java.sql.*;

public class DatabaseUtility {

    private final String username = "IceCodeTruckers";
    private final String password = "Icecodetruckers!";
    private final String hostname = "icecodetruckers.cyjhszrx5hbj.us-west-1.rds.amazonaws.com";
    private final String port = "3306";
    private final String databaseName = "IceCodeTruckers";
    private final String jdburl = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName + "?user=" + username + "&password=" + password;
    private static Connection connection;
    public static User currentUser;


    private void getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(jdburl);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public ResultSet queryDatabase(String query) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public boolean insertDatabase(String insertStatement) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        int result = statement.executeUpdate(insertStatement);
        connection.close();
        return result > 0; //maybe return void?
    }

    public void insert(String table, String[] fields, String[] values) throws SQLException {
        String statement = "INSERT INTO "+table+" (";
        for(int i=0; i<fields.length-1;i++){
            statement+=fields[i]+", ";
        }
        statement += fields[fields.length-1]+") VALUES (";
        for(int i=0; i<values.length-1;i++){
            statement+=values[i]+", ";
        }
        statement += values[values.length-1]+");";
        insertDatabase(statement);
    }

    public void update(String table, String[] fields, String[] values, String lookUpKey, String lookUpValue) throws SQLException {
        String statement = "UPDATE "+table+" SET ";
        //TODO: Check to make sure fields and values are equal length
        for(int i=0; i<fields.length-1; i++){
            statement += fields[i]+"="+values[i]+", ";
        }
        statement += fields[fields.length-1]+" = "+values[values.length-1]+" WHERE "+lookUpKey+ " = "+lookUpValue+";";
        insertDatabase(statement);
    }

    public User getCurrentUser(){
        return currentUser;
    }
}
