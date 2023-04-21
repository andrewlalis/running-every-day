package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.RunRecord;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A pre-defined list of chart renderers that can be used.
 */
public final class DateSeriesCharts {
    public static ChartRenderer weight(DataSource dataSource, DateRange dateRange) {
        final String query = applyDateRange("SELECT weight, date FROM run", dateRange) + " ORDER BY date ASC";
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
        final String query = applyDateRange("SELECT * FROM run", dateRange) + " ORDER BY date ASC";
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

    public static ChartRenderer totalDistance(DataSource dataSource, DateRange dateRange) {
        final String query = applyDateRange("SELECT distance, date FROM run", dateRange) + " ORDER BY date ASC";
        return new DateSeriesChartRenderer(
                "Total Distance (Km)",
                Color.GREEN,
                () -> {
                    List<DateSeriesChartRenderer.Datapoint> items = Queries.findAll(
                            dataSource.conn(),
                            query,
                            rs -> new DateSeriesChartRenderer.Datapoint(
                                    rs.getInt(1) / 1000.0,
                                    LocalDate.parse(rs.getString(2))
                            )
                    );
                    double total = 0;
                    // If a start date is specified, first compute the total distance ran up until then.
                    if (dateRange != null && dateRange.start() != null) {
                        total = Queries.getLong(dataSource.conn(), "SELECT SUM(distance) FROM run WHERE date < '" + dateRange.start() + "'") / 1000.0;
                    }
                    List<DateSeriesChartRenderer.Datapoint> finalItems = new ArrayList<>(items.size());
                    for (var d : items) {
                        total += d.value();
                        finalItems.add(new DateSeriesChartRenderer.Datapoint(total, d.date()));
                    }
                    return finalItems;
                }
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

    private static String applyDateRange(String query, DateRange range) {
        String rangeConditions = buildDateRangeConditions(range);
        if (rangeConditions != null) {
            return query + " WHERE " + rangeConditions;
        }
        return query;
    }
}
