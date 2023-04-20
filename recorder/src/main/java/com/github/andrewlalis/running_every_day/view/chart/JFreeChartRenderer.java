package com.github.andrewlalis.running_every_day.view.chart;

import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class JFreeChartRenderer implements ChartRenderer {
    protected abstract JFreeChart getChart() throws Exception;

    @Override
    public void render(Graphics2D graphics, Rectangle2D area) {
        try {
            var chart = getChart();
            // Apply theme to the chart.
            chart.draw(graphics, area);
        } catch (Exception e) {
            graphics.setColor(Color.BLACK);
            graphics.setBackground(Color.WHITE);
            graphics.fill(area);
            graphics.drawString("Error: " + e.getMessage(), 20, 40);
        }
    }
}
