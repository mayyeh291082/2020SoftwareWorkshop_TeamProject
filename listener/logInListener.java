package listener;

import serverClient.ClientButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import HomePage.HomePage;
import HomePage.loginPage;

/**
 * a listener for login button
 */
public class logInListener implements ActionListener {
    private ClientButton client;
    public static String[] onlinePlayers;

    public logInListener(ClientButton client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String u = loginPage.username2.getText();
        String p = new String(loginPage.password2.getPassword());

        System.out.println("username: " + u);
        System.out.println("password: " + p);


        try {
            //checks for empty username and password fields
            if (u.isEmpty() && p.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a username and password");
                loginPage.uAlert2.setText("Please enter a valid username.");
                loginPage.cAlert2.setText("Please enter a valid password.");
                loginPage.cAlert2.setForeground(Color.RED);
                loginPage.uAlert2.setForeground(Color.RED);
                return;
            } else if (u.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a username");
                loginPage.uAlert2.setText("Please enter a valid username.");
                loginPage.cAlert2.setText("");
                loginPage.uAlert2.setForeground(Color.RED);
                return;
            } else if (p.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a password");
                loginPage.cAlert2.setText("Please enter a valid password.");
                loginPage.uAlert2.setText("");
                loginPage.cAlert2.setForeground(Color.RED);
                return;
            } else if (!checkC(u) && !checkC(p)) {
                loginPage.uAlert2.setText("Please enter valid character.");
                loginPage.cAlert2.setText("Please enter valid character.");
                loginPage.uAlert2.setForeground(Color.RED);
                loginPage.cAlert2.setForeground(Color.RED);
                return;
            } else if (!checkC(u)) {
                loginPage.uAlert2.setText("Please enter valid character.");
                loginPage.cAlert2.setText("");
                loginPage.uAlert2.setForeground(Color.RED);
                return;
            } else if (!checkC(p)) {
                loginPage.cAlert2.setText("Please enter valid character.");
                loginPage.uAlert2.setText("");
                loginPage.cAlert2.setForeground(Color.RED);
                return;
            } else { //username and password fields are both filled out
                client.getOutputToServer().writeUTF("login/" + u + "/" + p);
                client.getOutputToServer().flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            String result = client.getInputFromSever().readUTF();
            switch (result) {
                case "match": //username and password match in the database
                    client.setUsername(u);
                    //show success
                    JOptionPane.showMessageDialog(loginPage.frame, "Logged-in success!");
                    loginPage.frame.dispose();
                    this.onlinePlayers = client.getInputFromSever().readUTF().split(" ");
                    System.out.println(Arrays.toString(onlinePlayers));
                    HomePage hp = new HomePage(client);
                    //reset loginpage
                    loginPage.username2.setText("");
                    loginPage.password2.setText("");
                    loginPage.uAlert2.setText("");
                    loginPage.cAlert2.setText("");
                    loginPage.displayp2.setSelected(false);
                    loginPage.displayp2.setText("Display password");
                    loginPage.cl.show(loginPage.panelCont, "start");
                    break;
                case "noMatch": //password does not match the username
                    JOptionPane.showMessageDialog(loginPage.frame, "Invalid password");
                    loginPage.cAlert2.setText("Wrong password, please try again.");
                    loginPage.uAlert2.setText("");
                    loginPage.cAlert2.setForeground(Color.RED);
                    break;
                case "invalid": //the username is not in the database
                    JOptionPane.showMessageDialog(loginPage.frame, "Username does not exist");
                    loginPage.uAlert2.setText("Username doesn't exist, please try another one.");
                    loginPage.cAlert2.setText("");
                    loginPage.uAlert2.setForeground(Color.RED);
                    break;
                case "already": //the username is not in the database
                    JOptionPane.showMessageDialog(loginPage.frame, "Username already loggedin");
                    loginPage.uAlert2.setText("Username already loggedin, please try another one.");
                    loginPage.cAlert2.setText("");
                    loginPage.uAlert2.setForeground(Color.RED);
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String[] getOnlinePlayers() {
        return onlinePlayers;
    }

    public boolean checkC(String string) {
        String valid = "[a-zA-Z0-9]{1,20}";
        return string.matches(valid);
    }

}
