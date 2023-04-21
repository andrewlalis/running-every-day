package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.RunRecord;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

/**
 * A panel for displaying a table view of run records, and various controls for
 * navigating the data.
 */
public class RunRecordsPanel extends JPanel {
    private final DataSource dataSource;

    private final RunRecordTableModel tableModel;
    private final JTextField currentPageField;
    private final JButton firstPageButton;
    private final JButton previousPageButton;
    private final JButton nextPageButton;
    private final JButton lastPageButton;

    public RunRecordsPanel(DataSource dataSource) {
        super(new BorderLayout());
        this.dataSource = dataSource;
        this.tableModel = new RunRecordTableModel(dataSource);

        var table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(80);
        table.getColumnModel().getColumn(2).setMaxWidth(70);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(60);
        table.getColumnModel().getColumn(5).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setMaxWidth(80);
        for (int i = 0; i < 7; i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        var scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel(new GridLayout(1, 6));
        firstPageButton = new JButton("First Page");
        firstPageButton.addActionListener(e -> {
            tableModel.firstPage();
            updateButtonStates();
        });
        previousPageButton = new JButton("Previous Page");
        previousPageButton.addActionListener(e -> {
            tableModel.previousPage();
            updateButtonStates();
        });
        nextPageButton = new JButton("Next Page");
        nextPageButton.addActionListener(e -> {
            tableModel.nextPage();
            updateButtonStates();
        });
        lastPageButton = new JButton("Last Page");
        lastPageButton.addActionListener(e -> {
            tableModel.lastPage();
            updateButtonStates();
        });

        JPanel currentPagePanel = new JPanel();
        currentPagePanel.add(new JLabel("Current Page: "));
        this.currentPageField = new JTextField("1", 3);
        currentPagePanel.add(this.currentPageField);

        JPanel pageSizePanel = new JPanel();
        pageSizePanel.add(new JLabel("Page Size: "));
        JComboBox<Integer> pageSizeSelector = new JComboBox<>(new Integer[]{5, 10, 20, 50, 100, 500});
        pageSizeSelector.setSelectedItem(tableModel.getPagination().size());
        pageSizeSelector.addItemListener(e -> {
            int size = (int) e.getItem();
            tableModel.setPageSize(size);
            updateButtonStates();
        });
        pageSizePanel.add(pageSizeSelector);

        paginationPanel.add(firstPageButton);
        paginationPanel.add(previousPageButton);
        paginationPanel.add(currentPagePanel);
        paginationPanel.add(pageSizePanel);
        paginationPanel.add(nextPageButton);
        paginationPanel.add(lastPageButton);

        this.add(paginationPanel, BorderLayout.SOUTH);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addActionButton = new JButton("Add a Record");
        addActionButton.addActionListener(e -> {
            var dialog = new AddRunRecordDialog(SwingUtilities.getWindowAncestor(this), dataSource);
            dialog.setVisible(true);
            if (dialog.isAdded()) {
                tableModel.firstPage();
            }
        });
        actionsPanel.add(addActionButton);
        JButton generateRandomDataButton = new JButton("Generate Random Data");
        generateRandomDataButton.addActionListener(e -> generateRandomData());
        actionsPanel.add(generateRandomDataButton);
        this.add(actionsPanel, BorderLayout.NORTH);
    }

    private void updateButtonStates() {
        firstPageButton.setEnabled(tableModel.canGoToFirstPage());
        nextPageButton.setEnabled(tableModel.canGoToNextPage());
        previousPageButton.setEnabled(tableModel.canGoToPreviousPage());
        lastPageButton.setEnabled(tableModel.canGoToLastPage());
        currentPageField.setText(String.valueOf(tableModel.getPagination().page() + 1));
    }

    private void generateRandomData() {
        try {
            var c = dataSource.conn();
            Queries.update(c, "DELETE FROM run");
            final int daysToDo = 1000;
            LocalDate date = LocalDate.now().minusDays(daysToDo);
            Random rand = new Random();
            c.setAutoCommit(false);
            for (int i = 0; i < daysToDo; i++) {
                LocalTime startTime = LocalTime.of(rand.nextInt(5, 22), rand.nextInt(60));
                BigDecimal distance = BigDecimal.valueOf(rand.nextInt(1000, 15000), 3)
                        .divide(BigDecimal.valueOf(1000, 3), RoundingMode.UNNECESSARY);
                double avgPage = rand.nextDouble(300.0, 700.0);
                BigDecimal weight = BigDecimal.valueOf(rand.nextInt(75000, 110000), 3)
                        .divide(BigDecimal.valueOf(1000, 3), RoundingMode.UNNECESSARY);
                dataSource.runRecords().save(new RunRecord(
                        0,
                        date,
                        startTime,
                        distance,
                        Duration.ofSeconds((long) Math.floor(avgPage * distance.doubleValue())),
                        weight,
                        "Bleh"
                ));
                date = date.plusDays(1);
            }
            c.commit();
            c.setAutoCommit(true);
            tableModel.firstPage();
            updateButtonStates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
