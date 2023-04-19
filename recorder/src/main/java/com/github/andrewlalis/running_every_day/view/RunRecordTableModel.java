package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DataSource;
import com.github.andrewlalis.running_every_day.data.Pagination;
import com.github.andrewlalis.running_every_day.data.RunRecord;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RunRecordTableModel extends AbstractTableModel {
    private final DataSource dataSource;
    private List<RunRecord> records;
    private int cachedPageCount;
    private Pagination currentPage;

    public RunRecordTableModel(DataSource dataSource) {
        this.dataSource = dataSource;
        this.cachedPageCount = 0;
        this.records = new ArrayList<>(0);
        this.currentPage = new Pagination(0, 20);
    }

    public void setPageSize(int size) {
        this.currentPage = new Pagination(0, size, currentPage.sorts());
        loadPage();
    }

    public void firstPage() {
        this.currentPage = new Pagination(0, currentPage.size(), currentPage.sorts());
        loadPage();
    }

    public boolean canGoToFirstPage() {
        return currentPage.page() != 0;
    }

    public void lastPage() {
        int pageIdx = cachedPageCount > 0 ? cachedPageCount - 1 : 0;
        this.currentPage = new Pagination(pageIdx, currentPage.size(), currentPage.sorts());
        loadPage();
    }

    public boolean canGoToLastPage() {
        return currentPage.page() < cachedPageCount - 1;
    }

    public void nextPage() {
        this.currentPage = currentPage.nextPage();
        loadPage();
    }

    public boolean canGoToNextPage() {
        return currentPage.page() < cachedPageCount - 1;
    }

    public void previousPage() {
        this.currentPage = currentPage.previousPage();
        loadPage();
    }

    public boolean canGoToPreviousPage() {
        return currentPage.page() > 0;
    }

    public Pagination getPagination() {
        return currentPage;
    }

    public void loadPage() {
        try {
            var page = dataSource.runRecords().findAll(currentPage);
            cachedPageCount = (int) dataSource.runRecords().pageCount(currentPage.size());
            records = page.items();
            this.fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
            records = new ArrayList<>();
            currentPage = new Pagination(0, 20);
        }
    }

    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= records.size()) return null;
        RunRecord r = records.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> Long.toString(r.id());
            case 1 -> r.date().toString();
            case 2 -> r.startTime().toString();
            case 3 -> r.distanceKm().toPlainString();
            case 4 -> r.durationFormatted();
            case 5 -> r.weightKg().toPlainString();
            case 6 -> r.comment();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Id";
            case 1 -> "Date";
            case 2 -> "Start Time";
            case 3 -> "Distance (Km)";
            case 4 -> "Duration";
            case 5 -> "Weight (Kg)";
            case 6 -> "Comment";
            default -> "Unknown Value";
        };
    }
}
