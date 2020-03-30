package HomePage;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;


/**
 * to draw player panel
 }*/
public class PlayerPanel extends JPanel {

    // private static final long serialVersionUID = 1L;

    private JLabel opponentInfo = null;



    private JPanel playerBar;// on the top to show title of bar
    private JPanel playerBody;// on the center to show online players
    private JScrollPane listPane; // a scroll




    PlayerPanel(){

        playerBar = new JPanel();
        playerBody = new JPanel();
        listPane = new JScrollPane();



        listPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        playerBody.add(listPane);


        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(new EtchedBorder(), "Waiting Player List" ,TitledBorder.CENTER,TitledBorder.TOP));

        this.add(playerBar,BorderLayout.NORTH);
        this.add(playerBody,BorderLayout.CENTER);


    }


    public JLabel getOpponentInfo() {

        if (opponentInfo == null){
            opponentInfo = new JLabel("Current opponent: no opponent!");
        }
        return opponentInfo;
    }

}
