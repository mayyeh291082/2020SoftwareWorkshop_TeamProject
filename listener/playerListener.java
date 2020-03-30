package listener;
import HomePage.playerPage;
import serverClient.ClientButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * a listener for online player list button
 * @author YYT
 */
public class playerListener implements ActionListener {
    private ClientButton client;

    public playerListener(ClientButton client) {
        this.client = client;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    	String[][] usersAndStatus=null;
    	int size = 0;

    	try {
    		client.getOutputToServer().writeUTF("player" +"/"+ client.getUsername());
			client.getOutputToServer().flush();

			String[] player = client.getInputFromSever().readUTF().split("/");
			
			size = player.length;
			usersAndStatus = new String[size/2][2];
			
			for (int i = 0; i < player.length; i++) {
				if (i %2 == 0) {
					usersAndStatus[(i)/2][0] = player[i];
				} else {
					usersAndStatus[(i-1)/2][1] = player[i];
				}
			}
			playerPage playerPage = new playerPage(usersAndStatus);
	        playerPage.setVisible(true);
    	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
