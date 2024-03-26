import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnector. getConnection()) {
            String query = "SELECT * FROM Furnizori";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String idf = resultSet.getString("idf");
                    String numef = resultSet.getString("numef");
                    int stare = resultSet.getInt("stare");
                    String oras = resultSet.getString("oras");

                    // Utilizați valorile obținute pentru operațiunile dorite
                    System.out.println("IDF: " + idf + ", NUMEF: " + numef + ", STARE: " + stare + ", ORAS: " + oras);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
