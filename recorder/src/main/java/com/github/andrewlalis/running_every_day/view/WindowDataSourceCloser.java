package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.db.DataSource;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Simple listener that closes the given datasource when the window closes.
 */
public class WindowDataSourceCloser extends WindowAdapter {
    private final DataSource dataSource;

    public WindowDataSourceCloser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            dataSource.close();
            System.out.println("Data source closed");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
