package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DataSource;

import javax.swing.*;
import java.awt.*;

public class RecorderAppWindow extends JFrame {
    public RecorderAppWindow(DataSource dataSource) {
        super("Run-Recorder");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(buildGui(dataSource));
        this.setPreferredSize(new Dimension(1000, 600));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private Container buildGui(DataSource dataSource) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Run Records", new RunRecordsPanel(dataSource));
        tabbedPane.addTab("Aggregate Statistics", new JPanel());
        tabbedPane.addTab("Charts", new ChartsPanel());
        return tabbedPane;
    }
}
