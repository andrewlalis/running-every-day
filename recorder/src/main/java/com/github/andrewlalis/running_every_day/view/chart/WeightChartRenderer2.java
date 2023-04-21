package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class WeightChartRenderer2 extends DateSeriesChartRenderer {
    private final DataSource dataSource;

    public WeightChartRenderer2(DataSource dataSource) {
        super("Weight (Kg)", Color.BLUE);
        this.dataSource = dataSource;
    }

    @Override
    protected Datapoint[] getData() throws Exception {
        List<Datapoint> datapoints = Queries.findAll(
                dataSource.conn(),
                "SELECT weight, date FROM run ORDER BY date ASC LIMIT 50",
                rs -> new Datapoint(
                        rs.getInt(1) / 1000.0,
                        LocalDate.parse(rs.getString(2))
                )
        );
        return datapoints.toArray(new Datapoint[0]);
    }
}
