package listener;
import HomePage.HomePage;
import HomePage.loginPage;
import serverClient.ClientButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * a listener for logout button
 * @author georgian
 */
public class LogoutListener implements ActionListener {

    ClientButton client;

    public LogoutListener(ClientButton client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("heloooooo");
        String username = client.getUsername();
        try {
            client.getOutputToServer().writeUTF("logout/" + username);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {

            if(client.getInputFromSever().readUTF().equals("loggedOut")){
                JOptionPane.showMessageDialog(HomePage.frame, "Logout Successful");
                HomePage.frame.dispose();
                new loginPage(this.client);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}

