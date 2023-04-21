package com.github.andrewlalis.running_every_day.view.chart;

@FunctionalInterface
public interface DateSeriesDataGenerator {
    Iterable<DateSeriesChartRenderer.Datapoint> generate() throws Exception;
}
