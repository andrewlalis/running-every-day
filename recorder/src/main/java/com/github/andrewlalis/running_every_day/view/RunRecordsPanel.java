package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DataSource;
import com.github.andrewlalis.running_every_day.data.Pagination;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * A panel for displaying a table view of run records, and various controls for
 * navigating the data.
 */
public class RunRecordsPanel extends JPanel {
    private final DataSource dataSource;
    private final RunRecordTableModel tableModel = new RunRecordTableModel();
    private final JTextField currentPageField;
    private final JComboBox<Integer> pageSizeSelector;
    private final JButton firstPageButton;
    private final JButton previousPageButton;
    private final JButton nextPageButton;
    private final JButton lastPageButton;

    private Pagination currentPage;
    public RunRecordsPanel(DataSource dataSource) {
        super(new BorderLayout());
        this.dataSource = dataSource;
        this.currentPage = new Pagination(0, 20);

        var table = new JTable(tableModel);
        var scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel(new GridLayout(1, 6));
        firstPageButton = new JButton("First Page");
        firstPageButton.addActionListener(e -> {
            currentPage = new Pagination(0, currentPage.size(), currentPage.sorts());
            loadPage();
        });
        previousPageButton = new JButton("Previous Page");
        previousPageButton.addActionListener(e -> {
            currentPage = currentPage.previousPage();
            loadPage();
        });
        nextPageButton = new JButton("Next Page");
        nextPageButton.addActionListener(e -> {
            currentPage = currentPage.nextPage();
            loadPage();
        });
        lastPageButton = new JButton("Last Page");
        lastPageButton.addActionListener(e -> {
            try {
                long pageCount = dataSource.runRecords().pageCount(currentPage.size());
                if (pageCount <= 0) pageCount = 1;
                currentPage = new Pagination((int) (pageCount - 1), currentPage.size(), currentPage.sorts());
                loadPage();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JPanel currentPagePanel = new JPanel();
        currentPagePanel.add(new JLabel("Current Page: "));
        this.currentPageField = new JTextField("1", 3);
        // TODO: Add document change listener.
        currentPagePanel.add(this.currentPageField);

        JPanel pageSizePanel = new JPanel();
        pageSizePanel.add(new JLabel("Page Size: "));
        pageSizeSelector = new JComboBox<>(new Integer[]{5, 10, 20, 50, 100, 500});
        pageSizeSelector.setSelectedItem(this.currentPage.size());
        pageSizeSelector.addItemListener(e -> {
            currentPage = new Pagination(0, (Integer) e.getItem(), currentPage.sorts());
            loadPage();
        });
        pageSizePanel.add(pageSizeSelector);


        paginationPanel.add(firstPageButton);
        paginationPanel.add(previousPageButton);
        paginationPanel.add(currentPagePanel);
        paginationPanel.add(pageSizePanel);
        paginationPanel.add(nextPageButton);
        paginationPanel.add(lastPageButton);

        this.add(paginationPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::loadPage);
    }

    private void loadPage() {
        try {
            this.tableModel.setPage(dataSource.runRecords().findAll(this.currentPage));
            long pageCount = dataSource.runRecords().pageCount(this.currentPage.size());
            this.firstPageButton.setEnabled(this.currentPage.page() != 0);
            this.previousPageButton.setEnabled(this.currentPage.page() > 0);
            this.nextPageButton.setEnabled(this.currentPage.page() < pageCount - 1);
            this.lastPageButton.setEnabled(this.currentPage.page() != pageCount - 1);
            this.currentPageField.setText(String.valueOf(this.currentPage.page() + 1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
