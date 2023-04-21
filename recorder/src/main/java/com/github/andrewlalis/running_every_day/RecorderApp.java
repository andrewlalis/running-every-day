package com.github.andrewlalis.running_every_day;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.view.RecorderAppWindow;
import com.github.andrewlalis.running_every_day.view.WindowDataSourceCloser;

import javax.swing.*;
import java.sql.SQLException;

/**
 * The main application entrypoint.
 */
public class RecorderApp {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        System.out.println("Setup FlatLAF");
        try {
            DataSource dataSource = new DataSource("jdbc:sqlite:runs.db");
            System.out.println("Initialized SQLite3 datasource");
            var window = new RecorderAppWindow(dataSource);
            System.out.println("Initialized App Window");
            window.addWindowListener(new WindowDataSourceCloser(dataSource));
            System.out.println("Added App Window close listener");
            window.setVisible(true);
            System.out.println("Set App Window as visible");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to open database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
