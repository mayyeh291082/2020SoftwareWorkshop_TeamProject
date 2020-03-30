package serverClient;

/**
 * constants to be used over the whole game
 */
public interface GameConstants {

    public static int FIRSTPLAYER = 1; //player 1
    public static int SECONDPLAYER = 2; //player 2
    public static String X = "X"; // mark X
    public static String O = "O"; // mark O
    public static int X_WON = 1; // player 1 won the
    public static int O_WON = 2; // player 2 won the game
    public static int TIE = 3; // players tied (nno winner but the board is full)
    public static int KEEP_PLAYING = 4; // continue playing the current game session
    public static int START = 0;


}
