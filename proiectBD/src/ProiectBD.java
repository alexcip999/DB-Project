import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ProiectBD extends JFrame {
    private JPanel mainPanel;
    private JButton Button_3a;
    private JButton Button_3b;
    private JTable table1;
    private JButton Button_4a;
    private JButton Button_4b;
    private JButton Button_5a;
    private JButton Button_5b;
    private JButton Button_6a;
    private JButton Button_6b;

    ProiectBD(){
        super("Display Data");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Button_3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT *\n" +
                        "FROM Furnizori\n" +
                        "ORDER BY stare DESC, numef ASC;");
            }
        });

        Button_3b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT * \n" +
                        "FROM Componente\n" +
                        "WHERE masa BETWEEN 100 AND 500 AND oras = 'Cluj-Napoca';");
            }
        });
        Button_4a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT P.numep, C.numec, P.oras\n" +
                        "FROM Proiecte P\n" +
                        "JOIN Livrari L ON P.idp = L.idp\n" +
                        "JOIN Componente C ON L.idc = C.idc AND P.oras = C.oras;");
            }
        });
        Button_4b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT DISTINCT L1.idp AS idp1, L2.idp AS idp2\n" +
                        "FROM Livrari L1\n" +
                        "JOIN Livrari L2 ON L1.idf = L2.idf AND L1.idc = L2.idc;");
            }
        });
        Button_5a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT numec\n" +
                        "FROM Componente\n" +
                        "WHERE idc IN (\n" +
                        "    SELECT idc\n" +
                        "    FROM (\n" +
                        "        SELECT idc\n" +
                        "        FROM Livrari\n" +
                        "        WHERE idp IN (\n" +
                        "            SELECT idp\n" +
                        "            FROM Proiecte\n" +
                        "            WHERE oras = 'Bistrita'\n" +
                        "        )\n" +
                        "        ORDER BY cantitate\n" +
                        "    ) AS SubqueryAlias\n" +
                        ");\n");
            }
        });
        Button_5b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT numep\n" +
                        "FROM Proiecte\n" +
                        "WHERE oras = ANY (\n" +
                        "    SELECT oras\n" +
                        "    FROM Furnizori\n" +
                        "    WHERE idf = 'F001'\n" +
                        ");");
            }
        });
        Button_6a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT oras,\n" +
                        "       COUNT(DISTINCT idp) AS numar_proiecte,\n" +
                        "       COUNT(DISTINCT idc) AS numar_componente,\n" +
                        "       COUNT(DISTINCT idf) AS numar_furnizori\n" +
                        "FROM (\n" +
                        "    SELECT oras, idp, null as idc, null as idf FROM Proiecte\n" +
                        "    UNION ALL\n" +
                        "    SELECT oras, null as idp, idc, null as idf FROM Componente\n" +
                        "    UNION ALL\n" +
                        "    SELECT oras, null as idp, null as idc, idf FROM Furnizori\n" +
                        ") CombinedData\n" +
                        "GROUP BY oras;\n");
            }
        });
        Button_6b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDataFromTable("SELECT idc,\n" +
                        "\tMIN(cantitate) AS cantitate_minima,\n" +
                        "\tMAX(cantitate) AS cantitate_maxima\n" +
                        "FROM Livrari\n" +
                        "WHERE idc = 'C12'\n" +
                        "GROUP BY idc;");
            }
        });
    }

    private void displayDataFromTable(String query){
        try(Connection connection = DatabaseConnector.getConnection()){

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){
                int columnCount = resultSet.getMetaData().getColumnCount();

                DefaultTableModel tableModel = new DefaultTableModel();

                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
                }

                while(resultSet.next()){
                    Vector<Object> row = new Vector<>();
                    for(int i = 1; i <= columnCount; i++){
                        row.add(resultSet.getObject(i));
                    }
                    tableModel.addRow((row));
                }

                table1.setModel(tableModel);

            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }





    public static void main(String[] args) {
        ProiectBD pbd = new ProiectBD();
        pbd.setVisible(true);
    }

}



