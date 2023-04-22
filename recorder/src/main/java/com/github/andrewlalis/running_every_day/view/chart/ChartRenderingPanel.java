package com.github.andrewlalis.running_every_day.view.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

public class ChartRenderingPanel extends JPanel implements Consumer<ChartRenderer> {
    private ChartRenderer renderer;

    public void setRenderer(ChartRenderer renderer) {
        this.renderer = renderer;
        this.repaint();
    }

    public ChartRenderer getRenderer() {
        return renderer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (renderer != null) {
            Graphics2D g2 = (Graphics2D) g;
            Rectangle2D area = new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight());
            renderer.render(g2, area, 1);
        } else {
            g.drawString("No chart to render", 50, 50);
        }
    }

    @Override
    public void accept(ChartRenderer chartRenderer) {
        setRenderer(chartRenderer);
    }
}
