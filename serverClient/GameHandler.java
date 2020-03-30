package serverClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Server thread to read and respond to client requests and implement methods from the class DBConnect in order to access data
 * in the database. All communication between clients is first passed through this class. The client only has access to data in
 * the database by making requests to this class, following a protocol, and then this class retrieves the data from the database
 * and can send it to the client. The client has no direct access to the database.
 *
 * The class permits multiple users to play against each other in the game by checking the move of each player and then updating
 * the corresponding game board of the other user.
 */
public class GameHandler extends Thread implements GameConstants {

    private Socket player;
    private DBConnect db;
    private static String[] board = new String[9]; //XO grid represented in an array
    public static int boxOtherPlayerMovedTo; //The move that a player made in the game.

    private DataInputStream inputFromPlayer;
    private DataOutputStream outputToPlayer;
    private String[] userInfo; //array containing the digested request of the client.
    public static ArrayList<String> onlinePlayers = new ArrayList<>(); //all the players that are online and not playing a game.
    private static ArrayList<Integer> games = new ArrayList<>(); //the size is the number of users that are in a game.

    private static HashMap<String, DataInputStream> inputStreamHashMap = new HashMap<>(); //stores all online user's name along with their input stream.
    private static HashMap<String, DataOutputStream> outputStreamHashMap = new HashMap<>(); //stores all online user's name along with their output stream.
    public static HashMap<String, Integer> usersAndScores = new HashMap<>();

    //specific datastreams for each player in the game.
    private static DataInputStream inputFromX;
    private static DataInputStream inputFromO;
    private static DataOutputStream outputToX;
    private static DataOutputStream outputToO;

    /**
     * Constructor for GameHandler
     *
     * @param player the socket connection of the user
     * @param db     the connection to the database
     */
    public GameHandler(Socket player, DBConnect db) {
        this.player = player;
        this.db = db;

        //initialise an empty XO grid for the beginning of the game
        initialiseBoard();
    }

    public void run() {
        try {
            //initialise the client's data streams.
            inputFromPlayer = new DataInputStream(player.getInputStream());
            outputToPlayer = new DataOutputStream(player.getOutputStream());

            //infinite while loop to continuously check client requests
            while (true) {

                //read the request from the client
                userInfo = inputFromPlayer.readUTF().split("/");

                //the first index (0) will always be an instruction/label for what the client wants.
                switch (userInfo[0]){
                
                	//client tried to open online player list
            		case "player":
            			String s="";
            			for (int i = 0; i <onlinePlayers.size(); i++) {
            				String u = onlinePlayers.get(i);
            				int p = db.getPlaying(u);
            				String pl;
            				if (p == 0) {
            					pl = "Not playing";
            				} else {
            					pl = "Playing";
            				}
            				s += u +"/"+pl+"/";
            			}
            			outputToPlayer.writeUTF(s);
		        		break;	
                
                //client tried to login
                	case "score":
                		String scoreL = Integer.toString(usersAndScores.size()) +"/";
                		
        				Iterator<Map.Entry<String, Integer>> iter = GameHandler.usersAndScores.entrySet().iterator();
        		        while (iter.hasNext()) {
        		        	Map.Entry<String, Integer> entry = iter.next();
        		        	System.out.println(entry.getKey());
        		        	scoreL += "/"+entry.getKey()+"/"+entry.getValue().toString();
        		        }
                		
                        outputToPlayer.writeUTF(scoreL);
                		break;
                		

                    //client tried to login
                    case "login":
                        //System.out.println(userInfo[1]);
                        //System.out.println(userInfo[2]);
                        //check the username exists in the database
                        if (!db.checkExistanceOfUsername(userInfo[1])) {
                            outputToPlayer.writeUTF("invalid"); //username is not in the database
                        }
                        //check the user is not already logged in.
                        else if (db.checkIfUserIsLoggedIn(userInfo[1])) {
                            outputToPlayer.writeUTF("already"); //username is already logged in
                        }
                        //check the password matches the password stored for the username in the database
                        else if (db.checkUserPassword(userInfo[1], encrypt(userInfo[2]))) {
                        //else if (db.checkUserPassword(userInfo[1], userInfo[2])){
                            System.out.println("logged in: " + userInfo[1] + "\nwith password: " + db.getPassword(userInfo[1]));
                            //username and password match
                            outputToPlayer.writeUTF("match");
                            //add the user to the list of online users.
                            onlinePlayers.add(userInfo[1]);
                            //output all the players so that the homescreen contains all the online players names.
                            outputToPlayer.writeUTF(onlinePlayersString());
                            db.updateToLoggedIn(userInfo[1]);
                            //add the user's username to the Maps along with their input and output stream
                            inputStreamHashMap.put(userInfo[1], inputFromPlayer);
                            outputStreamHashMap.put(userInfo[1], outputToPlayer);
                            usersAndScores.put(userInfo[1], db.getScore(userInfo[1]));
                            //outputToPlayer.writeUTF("addScores");
                            System.out.println(onlinePlayersString());
                            System.out.println(db.checkIfUserIsLoggedIn(userInfo[1]));
                            
                        } else {
                            //password does not match the username in the database.
                            outputToPlayer.writeUTF("noMatch");
                            System.out.println("false"); //invalid password
                        }
                        break;


                        //logout user
                    case "logout":
                        String username = userInfo[1];
                        //update the user status in the database to logged out.
                        db.updateToLoggedOut(username);
                        outputToPlayer.writeUTF("loggedOut");
                        System.out.println("user is logged out if false: " + db.checkIfUserIsLoggedIn(username));
                        //remove the user from the list of online users.
                        onlinePlayers.remove(username);
                        usersAndScores.remove(username);
                        //remove the player from the Maps.
                        inputStreamHashMap.remove(username);
                        outputStreamHashMap.remove(username);
                        System.out.println("removed player from maps and lists");
                        break;


                        //create a user account
                    case "signup":
                        //check if the username is already taken by an existing user.
                        if (db.checkExistanceOfUsername(userInfo[1])) {
                            outputToPlayer.writeUTF("taken"); //username already in use.
                        }
                        //check that the password and confirm password match
                        else if (!checkPasswordsMatch(userInfo[2], userInfo[3])) {
                            outputToPlayer.writeUTF("noMatch"); //inputted 2 different passwords
                        }
                        //add the user to the database
                        else {
                            db.addUserToDB(userInfo[1], encrypt(userInfo[2]));
                            //db.addUserToDB(userInfo[1], userInfo[2]);
                            //add the user to list of online users
                            onlinePlayers.add(userInfo[1]);
                            outputToPlayer.writeUTF("success");
                            //add the user's username and input and output streams to the Maps
                            inputStreamHashMap.put(userInfo[1], inputFromPlayer);
                            outputStreamHashMap.put(userInfo[1], outputToPlayer);
                        }
                        break;


                        //play the game
                    case "game":
                        games.add(1);
                        //update the user's status in the game to playing
                        db.updateToPlayingGame(userInfo[1]);
                        //remove the user from the list of online users that aren't in a game
//                        onlinePlayers.remove(userInfo[1]);
                        outputStreamHashMap.forEach((user, dataStream) -> System.out.println("user: " + user));
                        System.out.println(games.toString());
                        String player1 = "";
                        String player2 = "";
                        //if the user is an odd number
                        
                        if (games.size() % 2 != 0) {
                            player1 = userInfo[1]; //get the username
                            //get the specific user's data streams from the Maps
                            outputToX = new DataOutputStream(outputStreamHashMap.get(player1));
                            inputFromX = new DataInputStream(inputStreamHashMap.get(player1));
                            //Send a protocol to inform the user they are player X
                            outputToPlayer.writeInt(FIRSTPLAYER);
                        }
                        //if the user is an even number
                        else {
                            player2 = userInfo[1]; //get the username
                            //Send a protocol to inform the user they are player O
                            outputToPlayer.writeInt(SECONDPLAYER);
                            //get the specific user's data streams from the Maps
                            outputToO = new DataOutputStream(outputStreamHashMap.get(player2));
                            inputFromO = new DataInputStream(inputStreamHashMap.get(player2));
                            //inform the opponent that another player has joined and to start the game
                            try {
                                outputToX.writeInt(START);
                                outputToX.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;


                        //quit game before it finished
                    case "quitGame":
                        games.remove(0);
                        //update the user status to not playing a game in the database
                        db.updateToNotPlayingGame(userInfo[1]);
                        break;


                        //played a full game
                    case "endGame":
                        games.remove(0);
                        //update the user status to not playing a game in the database
                        db.updateToNotPlayingGame(userInfo[1]);
                        int score = db.getScore(userInfo[1]);
                        if (userInfo[2].equals("win")) {
                            db.updateScore(userInfo[1], score + 3);
                            usersAndScores.replace(userInfo[1], score+3);
                            System.out.println(userInfo[1] +" had OG score of " + score + " and new score of " + db.getScore(userInfo[1]));
                        }
                        if (userInfo[2].equals("tie")){
                            db.updateScore(userInfo[1], score+1);
                            usersAndScores.replace(userInfo[1], score+1);
                            System.out.println(userInfo[1] +" had OG score of " + score + " and new score of " + db.getScore(userInfo[1]));
                        }
                        //log the user out once a game has finished
//                        db.updateToLoggedOut(userInfo[1]);
//                        System.out.println("logged out at end of game");
                        //remove the user from the list of online players
//                        onlinePlayers.remove(userInfo[1]);
                        //remove the users data streams from the Maps
//                        inputStreamHashMap.remove(userInfo[1]);
//                        outputStreamHashMap.remove(userInfo[1]);
                        break;


                        //X made a move in the game
                    case "Xmoved":
                        //recieve the number of the box that the user moved to.
                        boxOtherPlayerMovedTo = inputFromX.readInt();
                        System.out.println("got box that X moved into");
                        //copy the move into the corresponding array index.
                        board[boxOtherPlayerMovedTo] = X;

                        //check the overall game state
                        if(playerHasWon(X)){
                            //tell both players X wins the game
                            outputToX.writeInt(X_WON);
                            outputToO.writeInt(X_WON);
                            //update the XO board on player O's screen
                            outputToO.writeInt(boxOtherPlayerMovedTo);
                            outputToO.flush(); outputToX.flush();
                            //reset the board array
                            initialiseBoard();
                        } else if (boardIsFull()){
                            //tell both players the game is a tie
                            outputToX.writeInt(TIE);
                            outputToO.writeInt(TIE);
                            //update the XO board on player O's screen
                            outputToO.writeInt(boxOtherPlayerMovedTo);
                            outputToO.flush(); outputToX.flush();
                            //reset the board array
                            initialiseBoard();
                        } else {
                            //no winner so keep playing the game
                            outputToO.writeInt(KEEP_PLAYING);
                            //update the XO board on player O's screen
                            outputToO.writeInt(boxOtherPlayerMovedTo);
                            outputToO.flush();
                        }
                        break;


                        //O made a move in the game
                    case "Omoved":
                        //recieve the number of the box that the user moved to.
                        boxOtherPlayerMovedTo = inputFromO.readInt();
                        System.out.println("got box that O moved into");
                        //copy the move into the corresponding array index.
                        board[boxOtherPlayerMovedTo] = O;

                        //check the overall game state
                        if(playerHasWon(O)){
                            //tell both players O wins the game
                            outputToO.writeInt(O_WON);
                            outputToX.writeInt(O_WON);
                            //update the XO board pn player X's screen
                            outputToX.writeInt(boxOtherPlayerMovedTo);
                            outputToX.flush(); outputToO.flush();
                            //reset the board array
                            initialiseBoard();
                        } else {
                            //no winner so keep playing the game
                            outputToX.writeInt(KEEP_PLAYING);
                            //update the XO board on player X's screen
                            outputToX.writeInt(boxOtherPlayerMovedTo);
                            outputToX.flush();
                        }
                        break;

                    default:
                        break;
                }
                    }
                } catch (IOException ex) {
                	ex.printStackTrace();
                } finally {
                }
    }

    /**
     * Method to encrypt the password of a user
     * @param password The password to be encrypted
     * @return The encrypted password.
     */
    public static String encrypt(String password){
        String encryptedPassword = "";
        try{
            byte[] hash = SHA256Encrypt(password.getBytes());
            encryptedPassword = new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }


    /**
     * Method to convert a String into bytes
     * @param hash The password digested into Bytes
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] SHA256Encrypt(byte[] hash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(hash);
        return digest.digest();
    }


    /**
     * Method to check that the password inputted by the user also matches the confirmed password
     * inputted by the user when trying to sign-up
     * @param password The password
     * @param copyPassword The confirmed password
     * @return True if they match, otherwise false.
     */
    public static boolean checkPasswordsMatch (String password, String copyPassword){
        if (password.equals(copyPassword)) {
            return true;
        }
        return false;
    }

    /**
     * Method to convert the ArrayList of online players into a
     * String with a space in-between each username
     * @return
     */
    public String onlinePlayersString () {
        StringBuffer sb = new StringBuffer();
        //iterate through each player
        for (String player : onlinePlayers) {
            sb.append(player);
            sb.append(" ");
        }
        String players = sb.toString();
        return players;
    }

    /**
     * method to find out whether one of the players (X or O) has won the game by getting 3 of their marks consecutively in a row
     * either horizontally, vertically or diagonally
     * @param playerMark the mark of the player for whom we are checking if they won
     * @return true if the player has 3 marks in a row (win), otherwise false
     */
    private synchronized boolean playerHasWon(String playerMark){
        //horizontal XO board checks
        if((board[0].equals(playerMark) && board[1].equals(playerMark) && board[2].equals(playerMark)) ||
                (board[3].equals(playerMark) && board[4].equals(playerMark) && board[5].equals(playerMark)) ||
                (board[6].equals(playerMark) && board[7].equals(playerMark) && board[8].equals(playerMark))){
            return true;
        }
        //vertical XO board checks
        if((board[0].equals(playerMark) && board[3].equals(playerMark) && board[6].equals(playerMark)) ||
                (board[1].equals(playerMark) && board[4].equals(playerMark) && board[7].equals(playerMark)) ||
                (board[2].equals(playerMark) && board[5].equals(playerMark) && board[8].equals(playerMark))){
            return true;
        }
        //diagonal XO board checks
        if((board[0].equals(playerMark) && board[4].equals(playerMark) && board[8].equals(playerMark)) ||
                (board[2].equals(playerMark) && board[4].equals(playerMark) && board[6].equals(playerMark))){
            return true;
        }
        //else there is no winner yet
        return false;
    }

    /**
     * Method to reset the board to empty once a game is finished (won/tie)
     * so that should the players want to replay, the board is reset.
     */
    private synchronized void initialiseBoard() {
        for (int i = 0; i <= 8; i++) {
            board[i] = "";
        }
    }

    /**
     * method to find out whether all the boxes in the XO grid are filled with the players' marks or not
     * @return true if all the boxes are filled. Otherwise false if there is at least one box 'empty'
     */
    private synchronized boolean boardIsFull(){
        for(int i = 0; i <=8; i++){
            if(board[i].equals("")){
                return false;
            }
        }
        return true;
    }
    
}
