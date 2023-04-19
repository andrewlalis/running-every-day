package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.RunRecord;

import javax.swing.*;
import java.util.List;

public class RunRecordRowSorter extends RowSorter<RunRecord> {
    @Override
    public RunRecord getModel() {
        return null;
    }

    @Override
    public void toggleSortOrder(int column) {

    }

    @Override
    public int convertRowIndexToModel(int index) {
        return 0;
    }

    @Override
    public int convertRowIndexToView(int index) {
        return 0;
    }

    @Override
    public void setSortKeys(List<? extends SortKey> keys) {

    }

    @Override
    public List<? extends SortKey> getSortKeys() {
        return null;
    }

    @Override
    public int getViewRowCount() {
        return 0;
    }

    @Override
    public int getModelRowCount() {
        return 0;
    }

    @Override
    public void modelStructureChanged() {

    }

    @Override
    public void allRowsChanged() {

    }

    @Override
    public void rowsInserted(int firstRow, int endRow) {

    }

    @Override
    public void rowsDeleted(int firstRow, int endRow) {

    }

    @Override
    public void rowsUpdated(int firstRow, int endRow) {

    }

    @Override
    public void rowsUpdated(int firstRow, int endRow, int column) {

    }
}
