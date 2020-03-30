package serverClient;
import HomePage.HomePage;
import HomePage.loginPage;
import listener.GameCloseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * client side to start whole program
 */
public class ClientButton extends JPanel implements Runnable, GameConstants, Serializable {

    public String username;
    private Socket socket;

    //input and output streams to/from server
    private   DataOutputStream outputToServer;
    private   DataInputStream inputFromSever;





    private boolean myTurn = false; //indicates whether the player has the turn
    private String mark = ""; //mark for the current player
    private String opponentMark = ""; //mark for the opponent
    private int selectedButtonPosition; //the button that the player has clicked on


    private JButton[] buttons = new JButton[9]; //create grid
    private JLabel gameStatus = new JLabel();
    private JLabel statePlayerMark = new JLabel();
    public static JFrame frame;


    //to determine if game should keep going
    private boolean keepPlaying = true;

    //wait for player to mark a box
    private boolean waitingForPlayerToMove = true;

    private Thread thread;
    private int currentGameState;

    private HomePage homePage;



    //private class containing GUI components for the XO grid
    private class GUI extends JPanel {

        public GUI() {
            setLayout(new GridLayout(3, 3, 0, 0));
            setBorder(new LineBorder(Color.black, 1));
            initialiseBoard();
        }

        /*
         * method to build buttons and add actionListener to each one so it responds to clicks
         * The array of buttons form the board as such:
         * _0_|_1_|_2_
         * _3_|_4_|_5_
         * _6_|_7_|_8_
         */
        public void initialiseBoard() {
            for (int i = 0; i <= 8; i++) {
                buttons[i] = new JButton();
                buttons[i].setText("");
                buttons[i].addActionListener(new ClickListener());
                //adds this button to the JPanel
                add(buttons[i]);
            }
        }

        //class implementing the functional interface ActionListner so that we can get responses to mouse clicks on the buttons
        //that make up the XO grid of the game
        private class ClickListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButton selectedButton = (JButton) actionEvent.getSource(); //get the button that was clicked
                if(!myTurn){
                    JOptionPane.showMessageDialog(null, "Not your turn", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                if ((selectedButton.getText().equals("")) && myTurn) {
                    selectedButton.setForeground(Color.BLUE);
                    selectedButton.setText(mark); //put the players mark in the box
                    myTurn = false; //just made a move so now its not this players turn
                    gameStatus.setText("waiting for opponent to move"); //notify the player that it is now the opponent's turn
                    waitingForPlayerToMove = false; //player just made a move so now we are not waiting for that player to move
                } if (!selectedButton.getText().equals("") && myTurn) {
                    JOptionPane.showMessageDialog(null, "Box taken. Please choose another.","WARNING", JOptionPane.WARNING_MESSAGE);
                }
                //get the button number of the button pressed by the player and store it in instance variable
                if (actionEvent.getSource() == buttons[0]){
                    selectedButtonPosition = 0;
                } else if (actionEvent.getSource() == buttons[1]){
                    selectedButtonPosition = 1;
                } else if (actionEvent.getSource() == buttons[2]){
                    selectedButtonPosition = 2;
                } else if (actionEvent.getSource() == buttons[3]){
                    selectedButtonPosition = 3;
                } else if (actionEvent.getSource() == buttons[4]){
                    selectedButtonPosition = 4;
                } else if (actionEvent.getSource() == buttons[5]){
                    selectedButtonPosition = 5;
                } else if (actionEvent.getSource() == buttons[6]){
                    selectedButtonPosition = 6;
                } else if (actionEvent.getSource() == buttons[7]){
                    selectedButtonPosition = 7;
                } else if (actionEvent.getSource() == buttons[8]){
                    selectedButtonPosition = 8;
                }
            }
        }
    }


    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }



    public void quitGame(){
        for (int i = 0; i <= 8; i++) {
            buttons[i].setText("");
        }
        keepPlaying = false;
        frame.dispose();
        //homePage.CreateGame.setEnabled(true);
    }

    /**
     * method to reset all the buttons to being blank
     */
    public synchronized void restartGame() {
        for (int i = 0; i <= 8; i++) {
            buttons[i].setText("");
        }
    }

    /**
     * Constructor for a client
     * @param serverIP the IP address of the server we want to request to connect with
     */
    public ClientButton(String serverIP){

        try{
            //create a socket to connect to serverIP on a specific port number
           System.out.println("dd");
        	socket = new Socket(serverIP, 5554);
        	System.out.println("dddddd");
//            socket = new Socket(serverIP, 5555);
            //create input and output stream to send/receive data to/from server
            inputFromSever = new DataInputStream(socket.getInputStream());
            outputToServer = new DataOutputStream(socket.getOutputStream());
            
            loginPage login = new loginPage(this);

        } catch (UnknownHostException e) {
            System.exit(1);
            e.printStackTrace();
        } catch (IOException e) {
            System.exit(1);
            e.printStackTrace();
        }

    }

    /**
     * Getter for the client's data output stream
     * @return The data output stream
     */
    public DataOutputStream getOutputToServer(){
        return outputToServer;
    }


    /**
     * Getter for the client's data input stream
     * @return The data input stream
     */
    public DataInputStream getInputFromSever(){ return inputFromSever; }


    /**
     * Method to begin the Noughts and Crosses game.
     * It opens the initial GUI and starts the client thread for the game.
     * By calling start() on the thread, the run() method is executed.
     */
    public void startGame(){
        //control the game on a separate thread
        frame = new JFrame("Noughts and Crosses");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new GameCloseListener(this));
        frame.setBounds(300,300,300,300);
        gameStatus.setBorder(new LineBorder(Color.black, 1));
        gameStatus.setVerticalAlignment(JLabel.CENTER);
        gameStatus.setHorizontalAlignment(JLabel.CENTER);
        gameStatus.setText("Waiting for an opponent");
        statePlayerMark.setBorder(new LineBorder(Color.black, 1));
        statePlayerMark.setHorizontalAlignment(JLabel.CENTER);
        statePlayerMark.setVerticalAlignment(JLabel.CENTER);
        frame.add(new GUI(), BorderLayout.CENTER);
        frame.add(gameStatus, BorderLayout.SOUTH);
        frame.add(statePlayerMark, BorderLayout.NORTH);
        frame.setVisible(true);
        keepPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * method to close the client's resources
     */
    public void closeConnections(){
        try{
            inputFromSever.close();
            outputToServer.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is executed when the thread calls the start() method in the startGame() method.
     * it updates each player's XO board to provide them with the relevant information about the game.
     * Each time a player moves it sends the move to the game handler, which checks the move and then
     * updates the other player's board with that move. The 'turn' is then passed to the other player
     * so that players alternate turns to make a move on the board.
     */
    @Override
    public synchronized void run() {
        try{
            //server sends over which player has joined (player 1 or player 2)
            int player = 0;
            System.out.println(socket);
            System.out.println(mark+"fffffff");
            if(mark.equals("")) {
                player = inputFromSever.readInt();
                System.out.println("read from server");
            } else if (mark.equals(X)){
                player = FIRSTPLAYER;
            } else if (mark.equals(O)){
                player = SECONDPLAYER;
            }
            System.out.println(mark+"HHHHHHHHHHOOO");
            if(player == FIRSTPLAYER){
                System.out.println("received info from server that this is player 1");
                statePlayerMark.setText(getUsername() + ": YOU ARE PLAYER 'X'");
                gameStatus.setText("waiting for player O to join");

                if (opponentMark.equals("")) {
                    //receive info from server that another player has joined the game.
                    //This triggers player 1 (X) to start the game.
                    inputFromSever.readInt();
                    System.out.println("another player has joined the game");
                }

                mark = X; opponentMark = O;
                myTurn = true; //player X starts the game.
            }
            else if (player == SECONDPLAYER){
                System.out.println("received info from server that this is player 2");
                mark = O; opponentMark = X;
                statePlayerMark.setText(getUsername() + ": YOU ARE PLAYER 'O'");
                gameStatus.setText("waiting for player X to start the game");
            }

            //while the game is in play, keep sending/receiving each players move and updating the XO board.
            while(keepPlaying){
                if(player == FIRSTPLAYER){
                    System.out.println("Your turn to move");
                    gameStatus.setText("Your turn to move");
                    waitForPlayerToClickBox(); //wait for player X to click a box
                    System.out.println("You moved to box " + selectedButtonPosition);
                    sendBoxXClickedOn(); //send the box number that X moved into to the server
                    System.out.println("Player O's turn to move");
                    gameStatus.setText("Waiting for player O to move");
                    receiveCurrentGameState(); //board is updated with opponent's move/current game status
                    System.out.println("Received player O's move to box " + selectedButtonPosition);
                }
                else if (player == SECONDPLAYER){
                    System.out.println("Player X's turn to move");
                    gameStatus.setText("Waiting for player X to move");
                    receiveCurrentGameState(); //board is updated with opponent's move/current game status
                    System.out.println("Received player X's move to box " + selectedButtonPosition);
                    System.out.println("Your turn to move");
                    gameStatus.setText("Your turn to move");
                    waitForPlayerToClickBox(); //wait for player O to click a box
                    System.out.println("You moved to box " + selectedButtonPosition);
                    sendBoxOClickedOn(); //send the box number that X moved into to the server
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to wait for a player to make a move into a box on the XO grid.
     * Once they have, the variable waitingForPlayerToMove is reset to true so that
     * when the method is next called the while loop will be entered again until the
     * player makes a move.
     * @throws InterruptedException
     */
    private synchronized void waitForPlayerToClickBox() throws InterruptedException {
        while (waitingForPlayerToMove){
            Thread.sleep(50);
        }
        waitingForPlayerToMove = true;
    }

    /**
     * Method to send the box number that the player with mark X moved their mark into, to the
     * GameHandler using the data output stream.
     * @throws IOException
     */
    private synchronized void sendBoxXClickedOn() throws IOException{
        int buttonPressed = selectedButtonPosition;
        outputToServer.writeUTF("Xmoved");
        outputToServer.writeInt(buttonPressed); //send which button was pressed to the server
        outputToServer.flush();
    }

    /**
     * Method to send the box number that the player with mark O moved their mark into, to the
     * GameHandler using the data output stream.
     * @throws IOException
     */
    private synchronized void sendBoxOClickedOn() throws IOException{
        int buttonPressed = selectedButtonPosition;
        outputToServer.writeUTF("Omoved");
        outputToServer.writeInt(buttonPressed); //send which button was pressed to the server
        outputToServer.flush();
    }

    /**
     * Method to receive the number of the box that the opponent player has placed their mark into.
     * The opponent player's mark is then put into that box number on the current players XO board.
     * This way the board remains up to date after each player's move.
     *
     * @throws IOException
     */
    private synchronized void receiveOpponentsMove() throws IOException {
        int boxOtherPlayerMovedTo = inputFromSever.readInt();
        buttons[boxOtherPlayerMovedTo].setForeground(Color.RED);
        buttons[boxOtherPlayerMovedTo].setText(opponentMark);
    }

    public void enableButtons(){
        homePage.CreateGame.setEnabled(true);
        //homePage.Logout.setEnabled(true);
        //homePage.Scores.setEnabled(true);
    }
    /**
     * Method to receive the current state of the game from the GameHandler.
     * The method checks whether the state of the game is that a player has won,
     * or whether there is a tie, or if the game is to continue. In each case,
     * the board of the opponent is updated with the other player's latest move.
     *
     * @throws IOException
     */
    private synchronized void receiveCurrentGameState() throws IOException {
        //read the current game state now that a player has moved
        currentGameState = inputFromSever.readInt();

        if(currentGameState == X_WON){
            //X won the game so stop the current game
            keepPlaying = false;
            switch (mark) {
                case X:
                    gameStatus.setText("X YOU WIN!");
                    //print option pane asking if want to play again
                    int click = JOptionPane.showOptionDialog(null, "PLAYER 'X' YOU WIN!\nCongrats!", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
//                        HomePage.frame.dispose();
//                        new loginPage(this);
                        outputToServer.writeUTF("endGame/" +getUsername() +"/win");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                case O:
                    gameStatus.setText("X wins.");
                    receiveOpponentsMove();
                    //print option pane asking if want to play again
                    click = JOptionPane.showOptionDialog(null, "Player 'X' wins!\nBetter luck next time.", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
//                        HomePage.frame.dispose();
//                        new loginPage(this);
                        outputToServer.writeUTF("endGame/" +getUsername() +"/loose");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                default:
                    break;
            }
        }
        else if (currentGameState == O_WON){
            //O won the game so stop the current game
            keepPlaying = false;
            switch (mark){
                case O:
                    gameStatus.setText("PLAYER O YOU WIN! Want to play again?");
                    //print option pane asking if want to play again
                    int click = JOptionPane.showOptionDialog(null, "PLAYER 'O' YOU WIN!\nCongrats!", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
                        outputToServer.writeUTF("endGame/" +getUsername() +"/win");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                case X:
                    gameStatus.setText("Player O wins.");
                    //receive the players move so X can see the winning move
                    receiveOpponentsMove();
                    //print option pane asking if want to play again
                    click = JOptionPane.showOptionDialog(null, "Player 'O' wins!\nBetter luck next time.", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
                        outputToServer.writeUTF("endGame/" +getUsername() +"/loose");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                default:
                    break;
            }
        }
        else if (currentGameState == TIE){
            //no winner but board is full so stop playing
            keepPlaying = false;
            gameStatus.setText("TIE!");
            switch (mark){
                case O:
                    receiveOpponentsMove();
                    int click = JOptionPane.showOptionDialog(null, "TIE", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
                        outputToServer.writeUTF("endGame/" +getUsername() +"/tie");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                case X:
                    click = JOptionPane.showOptionDialog(null, "TIE", null , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (click == 0){
                        frame.dispose();
                        outputToServer.writeUTF("endGame/" +getUsername() +"/tie");
                        enableButtons();
                        mark = ""; opponentMark = "";
                    }
                    thread.interrupt();
                    break;
                default:
                    break;
            }
        }
        //no one has won and the board isn't full so keep playing the game
        else {
            receiveOpponentsMove();
            gameStatus.setText("your turn");
            myTurn = true;
        }
    }


    public static void main(String[] args) {
        new ClientButton("localhost");
    }
}

