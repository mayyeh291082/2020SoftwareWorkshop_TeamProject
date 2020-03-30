package serverClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


/**
 * Class to establish a connection with the database, write data to the database and retrieve data from the database.
 */
public class DBConnect {

    private String url;
    private String username;
    private String password;
    private String path = "db.properties";
    private Connection connection;

    //constructor
    public DBConnect() {
        // try with resources (resources will automatically close when try block finishes)
        try (FileInputStream input = new FileInputStream(new File(path))) {
            Properties props = new Properties();
            props.load(input);

            // String driver = (String) props.getProperty("driver");
            url = (String) props.getProperty("URL");
            username = (String) props.getProperty("username");
            password = (String) props.getProperty("password");
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("connection to db established");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that checks that the password input by the user
     * matches the stored password for the given username in the database
     * @param password the password entered
     * @param username the username entered
     * @throws IOException
     */
    public boolean checkUserPassword(String username, String password) throws IOException {
        String storedPassword = getPassword(username);
        if (storedPassword.equals(password)) {
            updateToLoggedIn(username);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Method that checks if a given username is stored in the database.
     * @param username The username.
     * @return True if the username is in the database, otherwise false.
     */
    public boolean checkExistanceOfUsername(String username){
        boolean result = false;
        // try with resources (resources will automatically close when try block finishes)
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT username FROM player WHERE username= ?")){
            //Initialises the select query
            selectStatement.setString(1, username);
            //Trying the select query
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                result = resultSet.next();
            }
            } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Method to add a user to the database.
     * @param username The username.
     * @param password The password.
     */
    public void addUserToDB(String username, String password){
        try(PreparedStatement insertStatement = connection.prepareStatement
                ("INSERT INTO player (username,password) VALUES (?,?) ");
             PreparedStatement insertStatement2 = connection.prepareStatement
                     ("INSERT INTO userstatus (username,loggedin,playing) VALUES (?,?,?) ");
             PreparedStatement insertStatement3 = connection.prepareStatement
                     ("INSERT INTO rank (username,score) VALUES (?,?) ");){

            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement2.setString(1, username);
            insertStatement2.setInt(2, 1);
            insertStatement2.setInt(3, 0); //initially a new user's playing status will be 0.
            insertStatement3.setString(1, username);
            insertStatement3.setInt(2, 0); //initially a new user's score will be 0.

            insertStatement.executeUpdate();
            insertStatement2.executeUpdate();
            insertStatement3.executeUpdate();

        } catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Method to check whether a user is already logged into the system.
     * @param username The username.
     * @return True if the user is logged in, otherwise false.
     */
    public boolean checkIfUserIsLoggedIn(String username){
        boolean loggedInResult = false;
        try(PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM userstatus WHERE username = ?")){
            selectStatement.setString(1, username);
            try(ResultSet resultSet = selectStatement.executeQuery()){
                while(resultSet.next()){
                    loggedInResult = resultSet.getBoolean("loggedin");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return loggedInResult;
    }


    /**
     * Method to check whether a user is currently playing a game of noughts and crosses.
     * @param username The username
     * @return True if the user is playing a game, otherwise false.
     */
    public boolean checkIfUserIsPlayingGame(String username){
        boolean result = false;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM userstatus WHERE username = ?")){
            selectStatement.setString(1, username);
            try(ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    result = resultSet.getBoolean("playing");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Method to update a users playing status to playing a game.
     * @param username The username.
     */
    public void updateToPlayingGame(String username){
        try(PreparedStatement updateStatement = connection.prepareStatement("UPDATE userstatus SET playing = 1 WHERE username = ? ")){
            updateStatement.setString(1, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to update a users playing status to not playing a game.
     * @param username The username.
     */
    public void updateToNotPlayingGame(String username){
        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE userstatus SET playing = 0 WHERE username = ? ")){
            updateStatement.setString(1, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateScore(String username, int newScore){
        try(PreparedStatement updateStatement = connection.prepareStatement("UPDATE rank SET score = ? WHERE username = ? ")){
            updateStatement.setInt(1, newScore);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public String[] getHighScoringUsers(String username){
//        String[] top5Scorers;
//        try(PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM rank ORDER BY score DESC LIMIT 5")){
//            top5Scorers = selectStatement.executeQuery();
//            } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return top5Scorers;
//    }

    /**
     * Method to update a user's status to logged in.
     * @param username The username.
     */
    public void updateToLoggedIn(String username){
        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE userstatus SET loggedin = 1 WHERE username = ? ")){
            updateStatement.setString(1, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to update a user's status to logged out.
     * @param username The username.
     */
    public void updateToLoggedOut(String username){
        try(PreparedStatement updateStatement = connection.prepareStatement("UPDATE userstatus SET loggedin = 0 WHERE username = ? ")){
            updateStatement.setString(1, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the password from the database given a username
     * @param username The username
     * @return The password corresponding to the username
     * @throws IOException
     */
    public String getPassword(String username) throws IOException {
        String password = "Didn't enter try block";
        try (PreparedStatement selectStatement = connection.prepareStatement ("SELECT username, password FROM player WHERE username= ?")) {
            selectStatement.setString(1, username);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    password = resultSet.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }


    /**
     * Method to retrieve a user's score from the database.
     * @param username The user's username
     * @return
     */
    public int getScore(String username) {
        int score = -1;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT username, score FROM rank WHERE username =?")){
            selectStatement.setString(1, username);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    score = resultSet.getInt("score");
                }
            }
        } catch (SQLException e){
                e.printStackTrace();
        }
        return score;
    }

    public int getPlaying(String username) {
    	int p = 0;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT playing FROM userstatus WHERE username =?")){
            selectStatement.setString(1, username);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    p = resultSet.getInt("playing");
                }
            }
        } catch (SQLException e){
                e.printStackTrace();
        }
        return p;
    }


    public static void main(String[] args) throws IOException {
        DBConnect db = new DBConnect();
        System.out.println(db.getPassword("tom"));
    }
}