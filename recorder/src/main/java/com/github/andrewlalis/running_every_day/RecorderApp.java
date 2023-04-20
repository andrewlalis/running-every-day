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
        try {
            DataSource dataSource = new DataSource("jdbc:sqlite:runs.db");
            var window = new RecorderAppWindow(dataSource);
            window.addWindowListener(new WindowDataSourceCloser(dataSource));
            window.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to open database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
