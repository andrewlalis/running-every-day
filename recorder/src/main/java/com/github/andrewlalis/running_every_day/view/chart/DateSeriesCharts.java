package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.RunRecord;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class DateSeriesCharts {
    public static ChartRenderer weight(DataSource dataSource, DateRange dateRange) {
        String baseQuery = "SELECT weight, date FROM run";
        String dateRangeConditions = buildDateRangeConditions(dateRange);
        if (dateRangeConditions != null) {
            baseQuery += " WHERE " + dateRangeConditions;
        }
        baseQuery += " ORDER BY date ASC";
        final String query = baseQuery;
        return new DateSeriesChartRenderer(
                "Weight (Kg)",
                Color.BLUE,
                () -> Queries.findAll(
                        dataSource.conn(),
                        query,
                        rs -> new DateSeriesChartRenderer.Datapoint(
                                rs.getInt(1) / 1000.0,
                                LocalDate.parse(rs.getString(2))
                        )
                )
        );
    }

    public static ChartRenderer pace(DataSource dataSource, DateRange dateRange) {
        String baseQuery = "SELECT * FROM run";
        String dateRangeConditions = buildDateRangeConditions(dateRange);
        if (dateRangeConditions != null) {
            baseQuery += " WHERE " + dateRangeConditions;
        }
        baseQuery += " ORDER BY date ASC";
        final String query = baseQuery;
        final var mapper = new RunRecord.Mapper();
        return new DateSeriesChartRenderer(
                "Pace (Min/Km)",
                Color.ORANGE,
                () -> Queries.findAll(
                        dataSource.conn(),
                        query,
                        rs -> {
                            var record = mapper.map(rs);
                            return new DateSeriesChartRenderer.Datapoint(
                                    record.averagePacePerKm().toMillis() / (1000.0 * 60.0),
                                    record.date()
                            );
                        }
                )
        );
    }

    private static String buildDateRangeConditions(DateRange dateRange) {
        if (dateRange == null || (dateRange.start() == null && dateRange.end() == null)) {
            return null;
        }
        List<String> conditions = new ArrayList<>(2);
        if (dateRange.start() != null) {
            conditions.add("date >= '" + dateRange.start() + "'");
        }
        if (dateRange.end() != null) {
            conditions.add("date <= '" + dateRange.end() + "'");
        }
        return String.join(" AND ", conditions);
    }
}
