package com.github.andrewlalis.running_every_day.view.chart.menu;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.view.chart.ChartRenderer;
import com.github.andrewlalis.running_every_day.view.chart.DateRangePanel;
import com.github.andrewlalis.running_every_day.view.chart.DateSeriesChartRendererFactory;

import java.util.function.Consumer;

public class DateSeriesChartMenuItem extends ChartMenuItem {
    private final DateRange defaultDateRange;
    private final DateRangePanel dateRangePanel;

    public DateSeriesChartMenuItem(String name, String description, Consumer<ChartRenderer> rendererConsumer, DataSource dataSource, DateRange defaultRange, DateSeriesChartRendererFactory rendererFactory) {
        super(name, description, rendererConsumer);
        this.defaultDateRange = defaultRange;
        this.dateRangePanel = new DateRangePanel(defaultRange);
        this.add(dateRangePanel, 2);
        setupViewAction(e -> {
            rendererConsumer.accept(rendererFactory.getRenderer(dataSource, dateRangePanel.getSelectedDateRange()));
        });
    }
}
