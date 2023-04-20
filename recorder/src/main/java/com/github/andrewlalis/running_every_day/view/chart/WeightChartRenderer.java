package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.db.Queries;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;

public class WeightChartRenderer extends JFreeChartRenderer {
    private record WeightDatapoint(int weightGrams, LocalDate date){}

    private final DataSource dataSource;

    public WeightChartRenderer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected JFreeChart getChart() throws Exception {
        TimeSeries series = new TimeSeries("Weight", "Date", "Weight (Kg)");
        List<WeightDatapoint> datapoints = Queries.findAll(
                dataSource.conn(),
                "SELECT date, weight FROM run ORDER BY date ASC",
                rs -> new WeightDatapoint(
                        rs.getInt(2),
                        LocalDate.parse(rs.getString(1))
                )
        );
        for (var dp : datapoints) {
            series.add(new Day(dp.date.getDayOfMonth(), dp.date.getMonthValue(), dp.date.getYear()), dp.weightGrams);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        DateAxis domainAxis = new DateAxis("Date");
        domainAxis.setVerticalTickLabels(true);
        domainAxis.setDateFormatOverride(DateFormat.getDateInstance());
        NumberAxis rangeAxis = new NumberAxis("Weight (Kg)");
        rangeAxis.setAutoRange(true);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        return new JFreeChart(plot);
    }
}
