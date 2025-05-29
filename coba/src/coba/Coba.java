/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package coba;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;

public class Coba extends JFrame {
    public Coba() {
        setTitle("Tes JFreeChart");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Kategori A", 40);
        dataset.setValue("Kategori B", 60);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Contoh Pie Chart",
                dataset,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        add(chartPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Coba().setVisible(true);
        });
    }
}
