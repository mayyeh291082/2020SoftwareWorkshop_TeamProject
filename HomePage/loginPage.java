package HomePage;
import listener.signUpListener;
import serverClient.ClientButton;
import listener.logInListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import static javax.swing.GroupLayout.Alignment.*;

/**
 * a page to let user to create account or login
 * @author YYT
 */
public class loginPage extends JFrame implements ItemListener {

	public static JFrame frame = new JFrame("Noughts and Crosses");
    public static JPanel panelCont = new JPanel();
    JPanel panelStart = new JPanel();
    JPanel panelSignUp = new JPanel();
    JPanel panelLogIn = new JPanel();
    public static CardLayout cl = new CardLayout();
    private JButton signup, login, submit, submit2, goback, goback2;
    public static JToggleButton displayp, displayp2;
    public static JTextField username, username2;
    public static JLabel usernamel, passwordl, cpasswordl,
            uAlert, cAlert, cpAlert,usernamel2, passwordl2, uAlert2, cAlert2,
            e1,e2,e3,e4,e5,e6,e7,e8,valid,e9,valid2;
    JTextArea welcome;
    public static JPasswordField password, cpassword, password2;
    private JLabel connectInfo;// to show connected status with server
    private ClientButton client;



    public loginPage(ClientButton client) throws IOException {
        this.client = client;
        panelCont.setLayout(cl);

        signup = new JButton("Sign Up");
        signup.setBounds(175,100,150,50);

        login = new JButton("Log In");
        login.setBounds(175,250,150,50);

        goback = new JButton("Go Back");
        goback2 = new JButton("Go Back");

        setJToggleButton();
        setAction();

        submit = new JButton("Submit");
        submit.addActionListener(new signUpListener(this.client));

        submit2 = new JButton("Submit");
        submit2.addActionListener(new logInListener(this.client));

        welcome = new JTextArea(
                "ヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉ\n"
                        + "\n"
                        + "Welcome to Noughts and Crosses! \n"
                        + "\n"
                        + "Please sign-up an account to play.\n"
                        + "\n"
                        + "Or just logged-in with your existing account. \n"
                        + "\n"
                        + "ヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉヽ(●´∀`●)ﾉ");
        welcome.setRows(9);
        panelStart.add(welcome);
        panelStart.add(signup);
        panelStart.add(login);

        //sign-up page
        GroupLayout groupLayout = new GroupLayout(panelSignUp);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        panelSignUp.setLayout(groupLayout);

        username = new JTextField(15);
        usernamel = new JLabel("Enter username:");
        password = new JPasswordField(15);
        passwordl = new JLabel("Enter password:");
        cpassword = new JPasswordField(15);
        cpasswordl = new JLabel("Enter password again:");
        e1 = new JLabel("     ");
        uAlert = new JLabel("Please enter valid characters.");
        uAlert.setForeground(Color.GRAY);
        e2 = new JLabel("     ");
        cAlert = new JLabel("Please enter atleast 4 valid characters.");
        cAlert.setForeground(Color.GRAY);
        e3 = new JLabel("     ");
        cpAlert = new JLabel("     ");
        cAlert.setForeground(Color.GRAY);
        e6 = new JLabel("     ");
        valid = new JLabel("Invalid characters ' !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~'.");
        valid.setForeground(Color.GRAY);
        e8 = new JLabel("     ");

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(LEADING).
                        addComponent(usernamel).addComponent(e1).
                        addComponent(passwordl).addComponent(e2).
                        addComponent(cpasswordl).addComponent(e3).addComponent(e8).
                        addComponent(goback).addComponent(e6))
                .addGroup(groupLayout.createParallelGroup(TRAILING).
                        addComponent(username).addComponent(uAlert).
                        addComponent(password).addComponent(cAlert).
                        addComponent(cpassword).addComponent(cpAlert).addComponent(valid).
                        addComponent(displayp).addComponent(submit)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(usernamel).addComponent(username))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(e1).addComponent(uAlert))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(passwordl).addComponent(password))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(e2).addComponent(cAlert))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(cpasswordl).addComponent(cpassword))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(e3).addComponent(cpAlert))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(e8).addComponent(valid))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(goback).addComponent(displayp))
                .addGroup(groupLayout.createParallelGroup(BASELINE).
                        addComponent(e6).addComponent(submit)));




        //login page
        GroupLayout groupLayout2 = new GroupLayout(panelLogIn);
        groupLayout2.setAutoCreateGaps(true);
        groupLayout2.setAutoCreateContainerGaps(true);
        panelLogIn.setLayout(groupLayout2);

        username2 = new JTextField(15);
        usernamel2 = new JLabel("Enter username:");
        password2 = new JPasswordField(15);
        passwordl2 = new JLabel("Enter password:");
        e4 = new JLabel("     ");
        uAlert2 = new JLabel("Please enter valid characters.");
        uAlert2.setForeground(Color.GRAY);
        e5 = new JLabel("     ");
        cAlert2 = new JLabel("Please enter valid characters.");
        cAlert2.setForeground(Color.GRAY);
        e7 = new JLabel("     ");
        valid2 = new JLabel("Invalid characters ' !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~'.");
        valid2.setForeground(Color.GRAY);
        e9 = new JLabel("     ");

        groupLayout2.setHorizontalGroup(groupLayout2.createSequentialGroup()
                .addGroup(groupLayout2.createParallelGroup(LEADING).
                        addComponent(usernamel2).addComponent(e4).
                        addComponent(passwordl2).addComponent(e5).addComponent(e9).
                        addComponent(goback2).addComponent(e7))
                .addGroup(groupLayout2.createParallelGroup(TRAILING).
                        addComponent(username2).addComponent(uAlert2).
                        addComponent(password2).addComponent(cAlert2).addComponent(valid2).
                        addComponent(displayp2).addComponent(submit2)));

        groupLayout2.setVerticalGroup(groupLayout2.createSequentialGroup()
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(usernamel2).addComponent(username2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(e4).addComponent(uAlert2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(passwordl2).addComponent(password2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(e5).addComponent(cAlert2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(e9).addComponent(valid2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(goback2).addComponent(displayp2))
                .addGroup(groupLayout2.createParallelGroup(BASELINE).
                        addComponent(e7).addComponent(submit2)));



        panelCont.add(panelStart, "start");
        panelCont.add(panelSignUp, "sign");
        panelCont.add(panelLogIn, "log");


        signup.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "sign");
            }
        });

        login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "log");
            }
        });

        goback.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                username.setText("");password.setText("");cpassword.setText("");
                uAlert.setText("");cAlert.setText("");cpAlert.setText("");
                displayp.setSelected(false);
                displayp.setText("Display password");
                cl.show(panelCont, "start");
            }
        });

        goback2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                username2.setText("");password2.setText("");
                uAlert2.setText("");cAlert2.setText("");
                displayp2.setSelected(false);
                displayp2.setText("Display password");
                cl.show(panelCont, "start");
            }
        });

        frame.add(panelCont);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public JLabel getConnectInfo(){
        if(connectInfo == null) {
            connectInfo = new JLabel("Server Status: not log in");
        }
        return connectInfo;
    }

    private void setJToggleButton() {
        displayp = new JToggleButton("Display password", false);
        displayp2 = new JToggleButton("Display password", false);
    }
    private void setAction() {
        displayp.addItemListener(this);
        displayp2.addItemListener(this);
    }
    public void itemStateChanged(ItemEvent eve) {
        if (displayp.isSelected()) {
            displayp.setText("Cover password");
            String pw = new String(password.getPassword());
            cAlert.setText("Password: " +pw);
            String pw1 = new String(cpassword.getPassword());
            cpAlert.setText("Retyped Password: " +pw1);
        } else {
            displayp.setText("Display password");
            cAlert.setText("    ");
            cpAlert.setText("    ");
        }
        if (displayp2.isSelected()) {
            displayp2.setText("Cover password");
            String pw2 = new String(password2.getPassword());
            cAlert2.setText("Password: " +pw2);
        } else {
            displayp2.setText("Display password");
            cAlert2.setText("    ");
        }
    }
}