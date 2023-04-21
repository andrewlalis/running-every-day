package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.db.DataSource;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AggregateStatisticsPanel extends JPanel {
    private final DataSource dataSource;

    private final JTextField totalDistanceField;
    private final JTextField totalDurationField;

    public AggregateStatisticsPanel(DataSource dataSource) {
        super(new BorderLayout());
        this.dataSource = dataSource;

        totalDistanceField = new JTextField();
        totalDistanceField.setEditable(false);
        totalDurationField = new JTextField();
        totalDurationField.setEditable(false);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        statsPanel.add(new JLabel("Total Distance (Km)"));
        statsPanel.add(totalDistanceField);
        statsPanel.add(new JLabel("Total Duration"));
        statsPanel.add(totalDurationField);
        this.add(statsPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshStats());
        controlsPanel.add(refreshButton);
        this.add(controlsPanel, BorderLayout.NORTH);
    }

    public void init() {
        SwingUtilities.invokeLater(this::refreshStats);
    }

    private void refreshStats() {
        try {
            totalDistanceField.setText(dataSource.runRecords().getTotalDistanceKm().toPlainString() + " Km");
            var totalDuration = dataSource.runRecords().getTotalDuration();
            String durationStr = String.format(
                    "%d days, %d hours, %d minutes",
                    totalDuration.toDaysPart(),
                    totalDuration.toHoursPart(),
                    totalDuration.toMinutesPart()
            );
            totalDurationField.setText(durationStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
