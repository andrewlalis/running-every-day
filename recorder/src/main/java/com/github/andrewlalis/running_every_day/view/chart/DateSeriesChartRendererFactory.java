package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.db.DataSource;

@FunctionalInterface
public interface DateSeriesChartRendererFactory {
    ChartRenderer getRenderer(DataSource dataSource, DateRange range);
}
