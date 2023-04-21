package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.DateRange;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * An interactive panel for selecting a date range, with multiple different options.
 */
public class DateRangePanel extends JPanel {
    private record DateRangeChoice(DateRange range, String label) {
        @Override
        public String toString() {
            return label;
        }
    }

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JTextField startDateField = new JTextField();
    private final JCheckBox startDateEnabledCheckbox = new JCheckBox();
    private final JTextField endDateField = new JTextField();
    private final JCheckBox endDateEnabledCheckbox = new JCheckBox();

    private final DefaultComboBoxModel<DateRangeChoice> dateRangeChoiceModel = new DefaultComboBoxModel<>();

    public DateRangePanel(DateRange defaultRange) {
        super(new BorderLayout());
        tabbedPane.addTab("Exact Date", buildManualDatePanel(defaultRange));
        tabbedPane.addTab("Preset Date", buildPresetDatePanel(defaultRange));
        this.add(tabbedPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Date Range Selector");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        this.add(titleLabel, BorderLayout.NORTH);

        this.setMinimumSize(new Dimension(300, 120));
        this.setPreferredSize(this.getMinimumSize());
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    private JPanel buildManualDatePanel(DateRange defaultRange) {
        JPanel manualDatePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(3, 3, 3, 3);
        manualDatePanel.add(new JLabel("Start"), c);

        c.gridy = 1;
        manualDatePanel.add(new JLabel("End"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        manualDatePanel.add(startDateField, c);

        c.gridy = 1;
        manualDatePanel.add(endDateField, c);

        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0;
        manualDatePanel.add(startDateEnabledCheckbox, c);
        c.gridy = 1;
        manualDatePanel.add(endDateEnabledCheckbox, c);

        startDateEnabledCheckbox.addActionListener(e -> {
            startDateField.setEnabled(startDateEnabledCheckbox.isSelected());
        });
        endDateEnabledCheckbox.addActionListener(e -> {
            endDateField.setEnabled(endDateEnabledCheckbox.isSelected());
        });
        startDateEnabledCheckbox.setSelected(false);
        endDateEnabledCheckbox.setSelected(false);
        if (defaultRange != null) {
            if (defaultRange.start() != null) {
                startDateField.setText(defaultRange.start().toString());
                startDateEnabledCheckbox.setSelected(true);
            }
            if (defaultRange.end() != null) {
                endDateField.setText(defaultRange.end().toString());
                endDateEnabledCheckbox.setSelected(true);
            }
        }
        startDateField.setEnabled(startDateEnabledCheckbox.isSelected());
        endDateField.setEnabled(endDateEnabledCheckbox.isSelected());

        return manualDatePanel;
    }

    private JPanel buildPresetDatePanel(DateRange defaultRange) {
        JPanel panel = new JPanel();
        var choices = List.of(
                new DateRangeChoice(DateRange.lastNWeeks(1), "Last Week"),
                new DateRangeChoice(DateRange.lastNWeeks(2), "Last 2 Weeks"),
                new DateRangeChoice(DateRange.after(LocalDate.now().minusMonths(1)), "Last Month"),
                new DateRangeChoice(DateRange.after(LocalDate.now().minusMonths(3)), "Last 3 Months"),
                new DateRangeChoice(DateRange.after(LocalDate.now().minusYears(1)), "Last Year"),
                new DateRangeChoice(DateRange.unbounded(), "All")
        );
        dateRangeChoiceModel.addAll(choices);
        boolean assigned = false;
        for (var choice : choices) {
            if (choice.range().equals(defaultRange)) {
                dateRangeChoiceModel.setSelectedItem(choice);
                assigned = true;
                break;
            }
        }
        if (!assigned) {
            dateRangeChoiceModel.setSelectedItem(choices.get(0));
        }
        panel.add(new JComboBox<>(dateRangeChoiceModel));
        return panel;
    }

    public DateRange getSelectedDateRange() {
        if (tabbedPane.getSelectedIndex() == 0) {
            LocalDate start = null;
            LocalDate end = null;
            if (startDateEnabledCheckbox.isSelected()) {
                try {
                    start = LocalDate.parse(startDateField.getText());
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
            if (endDateEnabledCheckbox.isSelected()) {
                try {
                    end = LocalDate.parse(endDateField.getText());
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
            return new DateRange(start, end);
        } else {
            DateRangeChoice choice = (DateRangeChoice) dateRangeChoiceModel.getSelectedItem();
            return choice.range();
        }
    }
}
