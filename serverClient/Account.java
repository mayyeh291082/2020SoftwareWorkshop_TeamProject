package serverClient;
import java.net.Socket;

/**
 * help creating user data
 * @author georgina
 */
public class Account {
	String username;
	boolean login;
	boolean playing;
	int score;
	String password;
	Socket socket;
	
	
	public Account() {
		
	}
	/**
	 * @param username
	 * @param login
	 * @param playing
	 * @param score
	 * @param password
	 */
	public Account(String username, boolean login, boolean playing, int score, String password) {
		super();
		this.username = username;
		this.login = false;
		this.playing = false;
		this.score = score;
		this.password = password;
	}
	
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}
	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}






	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}






	/**
	 * @return the login
	 */
	public boolean isLogin() {
		return login;
	}






	/**
	 * @param login the login to set
	 */
	public void setLogin(boolean login) {
		this.login = login;
	}






	/**
	 * @return the playing
	 */
	public boolean isPlaying() {
		return playing;
	}






	/**
	 * @param playing the playing to set
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}






	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}






	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}






	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}






	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
    /**
     * Compare a provided password with the stored password.
     * @param password The provided password to which the password of
     * this object is compared.
     * @return true or false, if the password of the account 
     * agrees with the argument return true, if not return false.
     */
//	@Override
	public boolean checkPassword(String password) {
		return this.getPassword().equals(password);
	}

    /** 
     * Changes the password from old to new if the old password is
     * correct, else an error message is printed.
     * @param oldPassword The current password.
     * @param newPassword The future password.
     */
//	@Override
	public void changePassword(String oldPassword, String newPassword, String cnewPassword) {
		if (checkPassword(oldPassword)) {
			if (oldPassword.equals(newPassword)) {
				System.out.println("Please choose a new set of password.");
			} else {
				if (newPassword.equals(cnewPassword)) {
					setPassword(newPassword);
				} else {
					System.out.println("Please make sure typing new password twice correctly.");
					
				}
			}
			
		} else {
			System.out.println("Please make sure you got the right old password.");
		}
	}






	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
