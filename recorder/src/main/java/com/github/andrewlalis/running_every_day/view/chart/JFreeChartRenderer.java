package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.Resources;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public abstract class JFreeChartRenderer implements ChartRenderer {
    private JFreeChart lastChart = null;
    public static final ChartTheme standardChartTheme = getChartTheme();

    protected abstract JFreeChart getChart() throws Exception;
    protected void applyCustomStyles(JFreeChart chart) {}

    public void refresh() throws Exception {
        lastChart = getChart();
        standardChartTheme.apply(lastChart);
        applyCustomStyles(lastChart);
    }

    @Override
    public void render(Graphics2D graphics, Rectangle2D area) {
        try {
            if (lastChart == null) {
                refresh();
            }
            lastChart.draw(graphics, area);
        } catch (Exception e) {
            graphics.setColor(Color.RED);
            graphics.setBackground(Color.WHITE);
            graphics.fill(area);
            graphics.drawString("Error: " + e.getMessage(), 20, 40);
            e.printStackTrace();
        }
    }

    private static ChartTheme getChartTheme() {
        StandardChartTheme theme = new StandardChartTheme("Standard Theme");
        Font baseFont = Font.getFont("sans-serif");
        Font lightFont = Font.getFont("sans-serif");
        try {
            baseFont = Resources.readTTFFont("font/Roboto-Regular.ttf");
            lightFont = Resources.readTTFFont("font/Roboto-Light.ttf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        theme.setSmallFont(baseFont.deriveFont(10f));
        theme.setRegularFont(baseFont.deriveFont(12f));
        theme.setLargeFont(lightFont.deriveFont(18f));
        theme.setExtraLargeFont(lightFont.deriveFont(30f));

        theme.setChartBackgroundPaint(Color.WHITE);
        theme.setPlotBackgroundPaint(Color.WHITE);
        theme.setRangeGridlinePaint(Color.GRAY);
        theme.setDomainGridlinePaint(Color.GRAY);

        return theme;
    }

    protected static void applyStandardXYLineColor(JFreeChart chart, Paint paint) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, paint);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(3));
    }
}
