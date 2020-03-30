package listener;
import HomePage.HomePage;
import serverClient.ClientButton;
import serverClient.GameHandler;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * button listener to close game window
 * @author Georgina
 */
public class GameCloseListener implements WindowListener {
    private ClientButton client;

    public GameCloseListener(ClientButton client){
        this.client = client;
    }
    
    @Override
    public void windowClosing(WindowEvent e) {

        int click = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit the game?", null, JOptionPane.YES_NO_OPTION);
        if (click ==  JOptionPane.YES_OPTION){
            try {
                //client.restartGame();
                client.frame.dispose();
                new HomePage(client);

                client.getOutputToServer().writeUTF("quitGame/" + client.getUsername());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (click == JOptionPane.NO_OPTION){
            JOptionPane.getRootFrame().dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}
