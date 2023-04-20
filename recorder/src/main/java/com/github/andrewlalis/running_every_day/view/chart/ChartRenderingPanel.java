package com.github.andrewlalis.running_every_day.view.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ChartRenderingPanel extends JPanel {
    private final ChartRenderer renderer;

    public ChartRenderingPanel(ChartRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D area = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
        renderer.render(g2, area);
    }
}
