package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;

public class WeightChartRenderer extends JFreeChartRenderer {
    private record WeightDatapoint(double weightKg, LocalDate date){}

    private final DataSource dataSource;

    public WeightChartRenderer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected JFreeChart getChart() throws Exception {
        TimeSeries series = new TimeSeries("Weight", "Date", "Weight (Kg)");
        List<WeightDatapoint> datapoints = Queries.findAll(
                dataSource.conn(),
                "SELECT date, weight FROM run ORDER BY date ASC LIMIT 50",
                rs -> new WeightDatapoint(
                        rs.getInt(2) / 1000.0,
                        LocalDate.parse(rs.getString(1))
                )
        );
        double minWeight = Double.MAX_VALUE;
        double maxWeight = Double.MIN_VALUE;
        for (var dp : datapoints) {
            minWeight = Math.min(minWeight, dp.weightKg);
            maxWeight = Math.max(maxWeight, dp.weightKg);
            series.add(new Day(dp.date.getDayOfMonth(), dp.date.getMonthValue(), dp.date.getYear()), dp.weightKg);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        DateAxis domainAxis = new DateAxis();
        domainAxis.setVerticalTickLabels(true);
        domainAxis.setDateFormatOverride(DateFormat.getDateInstance());

        NumberAxis rangeAxis = new NumberAxis("Weight (Kg)");
        rangeAxis.setRangeWithMargins(minWeight, maxWeight);

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, new XYLineAndShapeRenderer());
        var chart = new JFreeChart("Weight", plot);
        chart.removeLegend();
        return chart;
    }

    @Override
    protected void applyCustomStyles(JFreeChart chart) {
        applyStandardXYLineColor(chart, Color.BLUE);
    }
}
