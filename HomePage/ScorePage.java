package HomePage;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * a page to show online player's score and ranking
 * @author Yamin
 */
public class ScorePage extends JFrame {

    private JScrollPane panel;
    private JTable table;
    private String[] columnNames;
    private static String [][] rowData; // the final string[][] with rank result, like format:{"rank","name","score"}
    private static String [][] rowDataWithoutRank;
    // the original string[][] without rank result, where we can get users' data from DB or server then store them in string[][],
    // only have username and score,  like format: {"name","score"}


    // constructor, need parameter rowDataWithoutRank
    public ScorePage(String[][] rowDataWithoutRank) {

        columnNames = new String[]{ "Rank","Username", "Score"}; // column names in score table
        rowData = rank(rowDataWithoutRank);  // every row data with rank result

        table = new JTable(rowData,columnNames); // JTable to show
        table.setBackground(new Color(240,255,255));

        // to make data shown on the center of table
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        // define frame size anf title
        setTitle("ScorePage");
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
    public static String[][] rank(String[][] rowDataWithoutRank){
        int a = rowDataWithoutRank.length;
        String[][] rowData= new String[a][3];

        if (rowDataWithoutRank.length != 1) {
        	for(int i=0;i< rowDataWithoutRank.length;i++){
                for(int j = i+1; j<rowDataWithoutRank.length;j++){
                    if(Integer.parseInt(rowDataWithoutRank[i][1]) < Integer.parseInt( rowDataWithoutRank[j][1])){
                        String[] temp = rowDataWithoutRank[j];
                        rowDataWithoutRank[j] = rowDataWithoutRank[i];
                        rowDataWithoutRank[i] = temp;
                    } 
                }
            }
        } 
        
        for(int i = 0; i<rowDataWithoutRank.length;i++){
            rowData[i][0] = Integer.toString(i+1);
            rowData[i][1] = rowDataWithoutRank[i][0];
            rowData[i][2] = rowDataWithoutRank[i][1];
        }
        return rowData;
    }
}
