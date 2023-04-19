package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.Page;
import com.github.andrewlalis.running_every_day.data.RunRecord;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RunRecordTableModel extends AbstractTableModel {
    private List<RunRecord> records;

    public RunRecordTableModel() {
        this.records = new ArrayList<>(0);
    }

    public void setPage(Page<RunRecord> page) {
        this.records = page.items();
        this.fireTableDataChanged();
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
        DecimalFormat singleDigitFormat = new DecimalFormat("#,##0.0");
        return switch (columnIndex) {
            case 0 -> Long.toString(r.id());
            case 1 -> r.date().toString();
            case 2 -> r.startTime().toString();
            case 3 -> r.distanceKm().toPlainString();
            case 4 -> r.duration().toString();
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
