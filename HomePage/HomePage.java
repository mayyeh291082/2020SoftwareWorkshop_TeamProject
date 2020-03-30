package HomePage;
import listener.CreateListener;
import listener.HomePageCloseListener;
import listener.ScoreListener;
import listener.playerListener;
import serverClient.ClientButton;
import listener.LogoutListener;
import javax.swing.*;
import java.awt.*;

/**
 * a page after user login to access the game
 * @author Yamin
 */
public class HomePage extends JFrame {
    public static JFrame frame;
    private ClientButton client;

    public static JButton CreateGame;
    private JButton Logout;
    private JButton Scores;
    private JButton OnlinePlayers;
    private JPanel MessageBar;
    private JPanel InfoBar;
    private ImageIcon background;
    public static JLabel connectInfo;// to show connected status with server
    public static JLabel  Message;
    public static final Color color2 = new Color(160,191,124);



    public HomePage(ClientButton client){

       this.client = client;
        frame = new JFrame();
        frame.setTitle("HomePage");

        //insert background picture for frame
        background = new ImageIcon("/Users/YYT/Desktop/eclipse-workspace/javaTProject/src/picture7.jpg");
        JLabel label = new JLabel(background);
        frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        JPanel jp =(JPanel)frame.getContentPane();
        jp.setOpaque(false);


        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new HomePageCloseListener(this.client));


        CreateGame = new JButton("PlayGame");
        CreateGame.setBounds(175,80,150,50);
        CreateGame.addActionListener(new CreateListener(this.client));
        CreateGame.setBackground(color2);
        CreateGame.setOpaque(true);

        OnlinePlayers = new JButton("OnlinePlayers");
        OnlinePlayers.setBounds(175,160,150,50);
        OnlinePlayers.addActionListener(new playerListener(this.client));
        OnlinePlayers.setBackground(color2);
        OnlinePlayers.setOpaque(true);



        InfoBar = new JPanel();
        InfoBar.setBounds(20,410,450,30);
        InfoBar.add(this.getConnectInfo());
        InfoBar.setOpaque(false);



        MessageBar = new JPanel();
        MessageBar.setBounds(20,430,450,30);
        MessageBar.add(this.getMessage());
        MessageBar.setOpaque(false);



        Scores = new JButton("Scores");
        Scores.setBounds(175,240,150,50);
        Scores.addActionListener( new ScoreListener(client));
        Scores.setBackground(color2);
        Scores.setOpaque(true);

        Logout = new JButton("Log Out");
        Logout.setBounds(175,320,150,50);
        Logout.addActionListener(new LogoutListener(client));
        Logout.setBackground(color2);
        Logout.setOpaque(true);




        frame.add(OnlinePlayers);
        frame.add(CreateGame);
        frame.add(Logout);
        frame.add(Scores);
        frame.add(InfoBar);
        frame.add(MessageBar);

    }

    public JLabel getConnectInfo(){
        connectInfo = new JLabel(this.client.getUsername() + ": logged-in");
        return connectInfo ;
    }

    public JLabel getMessage(){
        Message = new JLabel("Choose an option from the buttons above");
        return Message ;
    }

}