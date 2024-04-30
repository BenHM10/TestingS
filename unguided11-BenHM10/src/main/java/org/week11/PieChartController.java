package org.week11;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PieChartController implements Initializable {
    @FXML
    private PieChart pieChart;

    private final String DB_URL = "jdbc:sqlite:catatanku.db";
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect to the database
        getConnection();

        // Fetch data and create pie chart
        createPieChart();
    }

    private void getConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection error
        }
    }

    private void createPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String query = "SELECT kategori, COUNT(*) as count FROM catatan GROUP BY kategori";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String kategori = resultSet.getString("kategori");
                int count = resultSet.getInt("count");
                pieChartData.add(new PieChart.Data(kategori, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }

        // Create pie chart
        pieChart.setData(pieChartData);
        pieChart.setTitle("Catatan Distribution");
    }
}
