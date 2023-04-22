package com.github.andrewlalis.running_every_day.view.chart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface ChartRenderer {
    void render(Graphics2D graphics, Rectangle2D area, float textScale);
}
