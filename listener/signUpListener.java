package listener;
import HomePage.HomePage;
import HomePage.loginPage;
import serverClient.ClientButton;
import serverClient.DBConnect;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * a listener for sign-up button
 */
public class signUpListener implements ActionListener {
    DBConnect dbConnect;
    String[] link;
    ClientButton client;

    public signUpListener(ClientButton client){
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String u = loginPage.username.getText();
        String p = new String (loginPage.password.getPassword());
        String cp = new String (loginPage.cpassword.getPassword());

        try{
            if(u.isEmpty() && p.isEmpty() && cp.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a username and password");
                loginPage.uAlert.setText("Please enter your username.");
                loginPage.uAlert.setForeground(Color.RED);
                loginPage.cAlert.setText("Please enter your password.");
                loginPage.cAlert.setForeground(Color.RED);
                loginPage.cpAlert.setText("Please enter your password again.");
                loginPage.cpAlert.setForeground(Color.RED);
                return;
            } else if (u.isEmpty() && p.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a password");
                loginPage.uAlert.setText("Please enter your username.");
                loginPage.uAlert.setForeground(Color.RED);
                loginPage.cAlert.setText("Please enter your password.");
                loginPage.cAlert.setForeground(Color.RED);
                loginPage.cpAlert.setText("");
                return;
            } else if (p.isEmpty() && cp.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a password");
                loginPage.cAlert.setText("Please enter your password.");
                loginPage.cAlert.setForeground(Color.RED);
                loginPage.cpAlert.setText("Please enter your password again.");
                loginPage.cpAlert.setForeground(Color.RED);
                loginPage.uAlert.setText("");
                return;
            } else if (u.isEmpty() && cp.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a password");
                loginPage.uAlert.setText("Please enter your username.");
                loginPage.uAlert.setForeground(Color.RED);
                loginPage.cpAlert.setText("Please enter your password again.");
                loginPage.cpAlert.setForeground(Color.RED);
                loginPage.cAlert.setText("");
                return;
            } else if (u.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a username");
                loginPage.uAlert.setText("Please enter your username.");
                loginPage.uAlert.setForeground(Color.RED);
                loginPage.cAlert.setText("");loginPage.cpAlert.setText("");
                return;
            } else if (p.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter your password in both fields");
                loginPage.cAlert.setText("Please enter your password.");
                loginPage.cAlert.setForeground(Color.RED);
                loginPage.uAlert.setText("");loginPage.cpAlert.setText("");
                return;
            } else if (cp.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter your password in both fields");
                loginPage.cpAlert.setText("Please enter your password again.");
                loginPage.cpAlert.setForeground(Color.RED);
                loginPage.uAlert.setText("");loginPage.cAlert.setText("");
                return;
            } else if (!checkC(u) && !checkP(p)) {
                loginPage.uAlert.setText("Please enter valid character.");
                loginPage.cAlert.setText("Please enter 4 valid characters.");
                loginPage.uAlert.setForeground(Color.RED);
                loginPage.cAlert.setForeground(Color.RED);
                return;
            } else if (!checkC(u)) {
                loginPage.uAlert.setText("Please enter valid character.");
                loginPage.cAlert.setText("");loginPage.cpAlert.setText("");
                loginPage.uAlert.setForeground(Color.RED);
                return;
            } else if (!checkP(p)) {
                loginPage.cAlert.setText("Please enter 4 valid characters.");
                loginPage.uAlert.setText("");loginPage.cpAlert.setText("");
                loginPage.cAlert.setForeground(Color.RED);
                return;
            } else {
                client.getOutputToServer().writeUTF("signup/" + u + "/" + p + "/" + cp);
                client.getOutputToServer().flush();
                
                String result = client.getInputFromSever().readUTF();
                if (result.equals("success")) {
                    if (!p.equals(cp)) {
                        loginPage.cpAlert.setText("It doesn't match with password, please check again.");
                        loginPage.uAlert.setText("");loginPage.cAlert.setText("");
                        loginPage.cpAlert.setForeground(Color.RED);
                    } else {
                        client.setUsername(u);
                        JOptionPane.showMessageDialog(loginPage.frame, "Sign-up success!");
                        new HomePage(this.client);
                        loginPage.frame.dispose();
                        //reset signup page
                        loginPage.username.setText("");loginPage.password.setText("");loginPage.cpassword.setText("");
                        loginPage.uAlert.setText("");loginPage.cAlert.setText("");loginPage.cpAlert.setText("");
                        loginPage.displayp.setSelected(false);
                        loginPage.displayp.setText("Display password");
                        loginPage.cl.show(loginPage.panelCont, "start");
                    }
                }
                else if (result.equals("noMatch")) {
                    JOptionPane.showMessageDialog(null, "Passwords don't match!");
                    loginPage.cpAlert.setText("It doesn't match with password, please check again.");
                    loginPage.uAlert.setText("");loginPage.cAlert.setText("");
                    loginPage.cpAlert.setForeground(Color.RED);
                }
                else if (result.equals("taken")) {
                    if (!p.equals(cp)) {
                        JOptionPane.showMessageDialog(null, "Username already taken.\nPlease choose another.");
                        loginPage.uAlert.setText("Username already taken, please try another one.");
                        loginPage.cpAlert.setText("It doesn't match with password, please check again.");
                        loginPage.cAlert.setText("");
                        loginPage.uAlert.setForeground(Color.RED);
                        loginPage.cpAlert.setForeground(Color.RED);
                    } else {
                        JOptionPane.showMessageDialog(null, "Username already taken.\nPlease choose another.");
                        loginPage.uAlert.setText("Username already taken, please try another one.");
                        loginPage.cpAlert.setText("");loginPage.uAlert.setText("");
                        loginPage.uAlert.setForeground(Color.RED);
                    }
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkC(String string) {
        String valid = "[a-zA-Z0-9]{1,20}";
        return string.matches(valid);
    }

    public boolean checkP(String string) {
        String valid = "[a-zA-Z0-9]{4,20}";
        return string.matches(valid);
    }

}



