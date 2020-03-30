package HomePage;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * a page to show online player and play status.
 * @author YYT
 */
public class playerPage extends JFrame {

    private JScrollPane panel;
    private JTable table;
    private String[] columnNames;
    private static String [][] rowData; // the final string[][] with online player result

    // constructor, need parameter rowDataWithoutOrder
    public playerPage(String[][] rowDataWithoutOrder) {

        columnNames = new String[]{ "Username", "Status"}; // column names in score table
        rowData = rank(rowDataWithoutOrder);  // every row data with online player result

        table = new JTable(rowData,columnNames); // JTable to show
        table.setBackground(new Color(240,255,255));

        // to make data shown on the center of table
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        // define frame size and title
        setTitle("PlayerList");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // use a scroll panel to realise scrolling
        panel = new JScrollPane(table);
        add(panel, BorderLayout.CENTER);

    }


    /**
     this is a method to rank the users' score and their name as descending order of score, and return a new String[][]
     with the rank result.

     in terms of this method, we just need change input String[][], and it can return new String[][] we expect.
     */
    public static String[][] rank(String[][] rowDataWithoutOrder){
        
        if (rowDataWithoutOrder.length != 1) {
        	for(int i=0;i< rowDataWithoutOrder.length;i++){
                for(int j = i+1; j<rowDataWithoutOrder.length;j++){
                    if(rowDataWithoutOrder[i][0].compareTo(rowDataWithoutOrder[j][0])>0){
                        String[] temp = rowDataWithoutOrder[j];
                        rowDataWithoutOrder[j] = rowDataWithoutOrder[i];
                        rowDataWithoutOrder[i] = temp;
                    } 
                }
            }
        } 
        return rowDataWithoutOrder;
    }
}
