package listener;
import serverClient.ClientButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * a listener for creating game button
 */
public class CreateListener implements ActionListener {
    private ClientButton client;

    public CreateListener(ClientButton client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //starts the game
        try {
            client.getOutputToServer().writeUTF("game/" + client.getUsername());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        client.startGame();

    }
}
