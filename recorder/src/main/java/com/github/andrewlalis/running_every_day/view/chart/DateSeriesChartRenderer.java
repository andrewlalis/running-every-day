package com.github.andrewlalis.running_every_day.view.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.text.DateFormat;
import java.time.LocalDate;

public abstract class DateSeriesChartRenderer extends JFreeChartRenderer {
    protected record Datapoint (double value, LocalDate date) {}

    private final String title;
    private final Paint linePaint;

    protected DateSeriesChartRenderer(String title, Paint linePaint) {
        this.title = title;
        this.linePaint = linePaint;
    }

    protected abstract Datapoint[] getData() throws Exception;

    @Override
    protected JFreeChart getChart() throws Exception {
        TimeSeries series = new TimeSeries("Series");
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (var d : getData()) {
            minValue = Math.min(minValue, d.value());
            maxValue = Math.max(maxValue, d.value());
            series.add(
                    new Day(
                            d.date().getDayOfMonth(),
                            d.date().getMonthValue(),
                            d.date().getYear()
                    ),
                    d.value(),
                    false
            );
        }
        XYDataset dataset = new TimeSeriesCollection(series);

        DateAxis domainAxis = new DateAxis();
        domainAxis.setVerticalTickLabels(true);
        domainAxis.setDateFormatOverride(DateFormat.getDateInstance());

        NumberAxis rangeAxis = new NumberAxis();
        rangeAxis.setRangeWithMargins(minValue, maxValue);

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, new XYLineAndShapeRenderer());
        var chart = new JFreeChart(title, plot);
        chart.removeLegend();
        return chart;
    }

    @Override
    protected void applyCustomStyles(JFreeChart chart) {
        applyStandardXYLineColor(chart, linePaint);
    }
}
