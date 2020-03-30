package listener;
import HomePage.ScorePage;
import serverClient.ClientButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * a listener for score page button
 * @author YYT
 */
public class ScoreListener implements ActionListener {
    private ClientButton client;

    public ScoreListener(ClientButton client) {
        this.client = client;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    	String[][] usersAndScores=null;
    	int size = 0;

    	try {
    		client.getOutputToServer().writeUTF("score" +"/"+ client.getUsername());
			client.getOutputToServer().flush();

			String[] L = client.getInputFromSever().readUTF().split("/");

			size = Integer.parseInt(L[0]);
			usersAndScores = new String[size][2];
			
			for (int i = 2; i < L.length; i++) {
				if (i %2 == 0) {
					usersAndScores[(i-2)/2][0] = L[i];
				} else {
					usersAndScores[(i-3)/2][1] = L[i];
				}
			}
			
			ScorePage scorePage = new ScorePage(usersAndScores);
	        scorePage.setVisible(true);
    	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}


