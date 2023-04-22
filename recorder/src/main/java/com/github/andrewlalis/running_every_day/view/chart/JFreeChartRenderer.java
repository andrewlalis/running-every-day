package com.github.andrewlalis.running_every_day.view.chart;

import com.github.andrewlalis.running_every_day.Resources;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public abstract class JFreeChartRenderer implements ChartRenderer {
    private JFreeChart lastChart = null;

    private final StandardChartTheme standardChartTheme = getChartTheme();
    private static final float SMALL_FONT_BASE_SIZE = 10;
    private static final float REGULAR_FONT_BASE_SIZE = 12;
    private static final float LARGE_FONT_BASE_SIZE = 18;
    private static final float EXTRA_LARGE_FONT_BASE_SIZE = 30;

    protected abstract JFreeChart getChart() throws Exception;
    protected void applyCustomStyles(JFreeChart chart, float textScale) {}

    @Override
    public void render(Graphics2D graphics, Rectangle2D area, float textScale) {
        if (textScale < 0) textScale = 1;
        try {
            if (lastChart == null) {
                lastChart = getChart();
            }
            setThemeTextScale(textScale);
            standardChartTheme.apply(lastChart);
            applyCustomStyles(lastChart, textScale);

            lastChart.draw(graphics, area);
        } catch (Exception e) {
            graphics.setColor(Color.RED);
            graphics.setBackground(Color.WHITE);
            graphics.fill(area);
            graphics.drawString("Error: " + e.getMessage(), 20, 40);
            e.printStackTrace();
        }
    }

    private static StandardChartTheme getChartTheme() {
        StandardChartTheme theme = new StandardChartTheme("Standard Theme");
        Font baseFont = Font.getFont("sans-serif");
        Font lightFont = Font.getFont("sans-serif");
        try {
            baseFont = Resources.readTTFFont("font/Roboto-Regular.ttf");
            lightFont = Resources.readTTFFont("font/Roboto-Light.ttf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        theme.setSmallFont(baseFont);
        theme.setRegularFont(baseFont);
        theme.setLargeFont(lightFont);
        theme.setExtraLargeFont(lightFont);

        theme.setChartBackgroundPaint(Color.WHITE);
        theme.setPlotBackgroundPaint(Color.WHITE);
        theme.setRangeGridlinePaint(Color.GRAY);
        theme.setDomainGridlinePaint(Color.GRAY);

        return theme;
    }

    private void setThemeTextScale(float scale) {
        standardChartTheme.setSmallFont(standardChartTheme.getSmallFont().deriveFont(scale * SMALL_FONT_BASE_SIZE));
        standardChartTheme.setRegularFont(standardChartTheme.getRegularFont().deriveFont(scale * REGULAR_FONT_BASE_SIZE));
        standardChartTheme.setLargeFont(standardChartTheme.getLargeFont().deriveFont(scale * LARGE_FONT_BASE_SIZE));
        standardChartTheme.setExtraLargeFont(standardChartTheme.getExtraLargeFont().deriveFont(scale * EXTRA_LARGE_FONT_BASE_SIZE));
    }

    protected static void applyStandardXYLineColor(JFreeChart chart, Paint paint, float textScale) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, paint);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(3 * textScale));

        XYPlot plot = chart.getXYPlot();
        BasicStroke stroke = new BasicStroke(
                textScale,
                BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER,
                1,
                new float[]{2 * textScale, 2 * textScale},
                0
        );
        plot.setDomainGridlineStroke(stroke);
        plot.setRangeGridlineStroke(stroke);
    }
}
