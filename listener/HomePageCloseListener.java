package listener;
import serverClient.ClientButton;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * button listener to close homepage window
 * @author georgina
 */
public class HomePageCloseListener implements WindowListener {
    private ClientButton client;

    public HomePageCloseListener(ClientButton client){
        this.client = client;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        try {
            client.getOutputToServer().writeUTF("logout/" + client.getUsername());
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
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
