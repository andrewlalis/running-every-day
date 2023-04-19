package com.github.andrewlalis.running_every_day.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ChartsPanel extends JPanel {
	private final JPanel drawingPanel = new JPanel();

	public ChartsPanel() {
		super(new BorderLayout());
		this.add(drawingPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(e -> draw());
		buttonPanel.add(drawButton);
		this.add(buttonPanel, BorderLayout.NORTH);
	}

	public void draw() {
		TimeSeriesCollection ds = new TimeSeriesCollection();
		TimeSeries ts = new TimeSeries("Data");
		ts.add(new Day(16, 4, 2023), 45);
		ts.add(new Day(17, 4, 2023), 50);
		ts.add(new Day(18, 4, 2023), 52);
		ts.add(new Day(19, 4, 2023), 65);
		ds.addSeries(ts);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Test XY Line Chart",
				"Date",
				"Value",
				ds
		);
		Graphics2D g2 = (Graphics2D) drawingPanel.getGraphics();
		Rectangle2D area = new Rectangle2D.Float(0, 0, drawingPanel.getWidth(), drawingPanel.getHeight());
		chart.draw(g2, area);
	}
}
