package com.PasswordManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Analyzer extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/passfort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TITLE_COLOR = new Color(255, 255, 255); // White title color

    private String username;
    private ChartPanel chartPanel;  // Reference to the chart panel
    private JPanel colorIndicatorsPanel; // Panel for the color indicators

    public Analyzer(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Create and add title label
        createTitleLabel();

        // Create the pie chart (hollow type)
        createPieChart();

        // Create and add the color indicators panel below the pie chart
        createColorIndicators();
    }

    private void createTitleLabel() {
        JLabel titleLabel = new JLabel("Password Analyzer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));  // Title font
        titleLabel.setForeground(TITLE_COLOR);  // Set title color
        titleLabel.setPreferredSize(new Dimension(getWidth(), 40));  // Make the title panel tall enough to be placed at the top
        titleLabel.setBackground(BACKGROUND_COLOR);
        titleLabel.setOpaque(true);
        add(titleLabel, BorderLayout.NORTH);  // Position the title at the top of the panel
    }

    private void createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Get password strength counts
        int[] counts = getPasswordStrengthCounts();

        dataset.setValue("Weak", counts[0]);
        dataset.setValue("Intermediate", counts[1]);
        dataset.setValue("Strong", counts[2]);

        // Create the donut (hollow) chart using RingPlot
        JFreeChart chart = ChartFactory.createRingChart(
                "", // Title (not shown)
                dataset,                         // Dataset
                false,                            // Don't include legend
                true,                             // Tooltips
                false                             // No URLs
        );

        // Customize chart
        chart.setBackgroundPaint(BACKGROUND_COLOR);
        RingPlot plot = (RingPlot) chart.getPlot(); // Cast to RingPlot for donut chart functionality
        plot.setSectionPaint("Weak", new Color(255, 99, 71)); // Red
        plot.setSectionPaint("Intermediate", new Color(255, 255, 102)); // Yellow
        plot.setSectionPaint("Strong", new Color(102, 255, 102)); // Green

        // Adjust the hole size to create the "hollow" effect
        plot.setSectionDepth(0.20); // This adjusts the thickness of the "ring" (donut)

        // Create and add the chart panel
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));  // Size of the chart
        chartPanel.setMouseWheelEnabled(true);  // Enable zooming with mouse wheel
        add(chartPanel, BorderLayout.CENTER);  // Add chart panel to the center of the panel
    }

    private void createColorIndicators() {
        colorIndicatorsPanel = new JPanel();
        colorIndicatorsPanel.setLayout(new GridLayout(1, 3)); // Layout for indicators
        colorIndicatorsPanel.setBackground(BACKGROUND_COLOR);

        // Add color indicators (labels) for Weak, Intermediate, Strong
        colorIndicatorsPanel.add(createColorIndicator("Weak", new Color(255, 99, 71)));
        colorIndicatorsPanel.add(createColorIndicator("Intermediate", new Color(255, 255, 102)));
        colorIndicatorsPanel.add(createColorIndicator("Strong", new Color(102, 255, 102)));

        add(colorIndicatorsPanel, BorderLayout.SOUTH); // Position color indicators at the bottom of the panel (below chart)
    }

    private JPanel createColorIndicator(String label, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel colorLabel = new JLabel(label);
        colorLabel.setForeground(TITLE_COLOR); // Set the label color to white
        panel.add(colorLabel);

        // Create a small colored square next to the label
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        panel.add(colorBox);

        return panel;
    }

    // Method to update the chart with new data
    public void updateChart() {
        // Remove the old chart
        remove(chartPanel);

        // Re-create and add the updated chart
        createPieChart();

        // Revalidate and repaint the panel to reflect the new chart
        revalidate();
        repaint();
    }

    private int[] getPasswordStrengthCounts() {
        int weakCount = 0, intermediateCount = 0, strongCount = 0;

        // Query the database for password strengths
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT strength FROM " + username);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int strength = rs.getInt("strength");

                switch (strength) {
                    case 0 -> weakCount++;
                    case 1 -> intermediateCount++;
                    case 2 -> strongCount++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new int[]{weakCount, intermediateCount, strongCount};
    }
}
