package org.week11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PieChartController implements Initializable {
    private final String DB_URL = "jdbc:catatanku.db";
    private Connection connection;
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getConnection();
        preparedData();
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection error
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection closure error
            }
        }
    }

    private void preparedData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT\n" +
                "    CASE\n" +
                "        WHEN nilai >= 0 AND nilai <= 20 THEN 'E'\n" +
                "        WHEN nilai >= 21 AND nilai <= 40 THEN 'D'\n" +
                "        WHEN nilai >= 41 AND nilai <= 60 THEN 'C'\n" +
                "        WHEN nilai >= 61 AND nilai <= 80 THEN 'B'\n" +
                "        WHEN nilai >= 81 AND nilai <= 100 THEN 'A'\n" +
                "        END AS mark,\n" +
                "    COUNT(*) AS jumlah\n" +
                "FROM\n" +
                "    mahasiswa\n" +
                "GROUP BY\n" +
                "    CASE\n" +
                "        WHEN nilai >= 0 AND nilai <= 20 THEN 'E'\n" +
                "        WHEN nilai >= 21 AND nilai <= 40 THEN 'D'\n" +
                "        WHEN nilai >= 41 AND nilai <= 60 THEN 'C'\n" +
                "        WHEN nilai >= 61 AND nilai <= 80 THEN 'B'\n" +
                "        WHEN nilai >= 81 AND nilai <= 100 THEN 'A'\n" +
                "        END;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String mark = resultSet.getString("mark");
                int jumlah = resultSet.getInt("jumlah");


                pieChartData.add(new PieChart.Data(mark, jumlah));
            }
            pieChart.setData(pieChartData);
            pieChart.setTitle("Jumlah Nilai berdasarkan Mark");
            //setting the direction to arrange the data
            pieChart.setClockwise(true);
            //Setting the length of the label line
            pieChart.setLabelLineLength(50);
            //Setting the labels of the pie chart visible
            pieChart.setLabelsVisible(true);
            //Setting the start angle of the pie chart
            pieChart.setStartAngle(180);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
    }
}

