package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.db.DataSource;

import javax.swing.*;
import java.awt.*;

public class RecorderAppWindow extends JFrame {
    private final RunRecordsPanel runRecordsPanel;
    private final AggregateStatisticsPanel aggregateStatisticsPanel;

    public RecorderAppWindow(DataSource dataSource) {
        super("Run-Recorder");
        this.runRecordsPanel = new RunRecordsPanel(dataSource);
        this.aggregateStatisticsPanel = new AggregateStatisticsPanel(dataSource);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setContentPane(buildGui(dataSource));
        this.setPreferredSize(new Dimension(1000, 600));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void initComponentData() {
        runRecordsPanel.init();
        aggregateStatisticsPanel.init();
    }

    private Container buildGui(DataSource dataSource) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Run Records", runRecordsPanel);
        tabbedPane.addTab("Aggregate Statistics", aggregateStatisticsPanel);
        tabbedPane.addTab("Charts", new ChartsPanel(dataSource));
        return tabbedPane;
    }
}
