package com.github.andrewlalis.running_every_day;

import com.formdev.flatlaf.FlatLightLaf;
import com.github.andrewlalis.running_every_day.data.DataSource;
import com.github.andrewlalis.running_every_day.view.RecorderAppWindow;

import java.sql.SQLException;

/**
 * The main application entrypoint.
 */
public class RecorderApp {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource("jdbc:sqlite:runs.db");
        FlatLightLaf.setup();
        var window = new RecorderAppWindow(dataSource);
        window.setVisible(true);
    }
}
