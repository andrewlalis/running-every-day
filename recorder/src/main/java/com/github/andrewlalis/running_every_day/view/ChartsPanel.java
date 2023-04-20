package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.view.chart.ChartRenderingPanel;
import com.github.andrewlalis.running_every_day.view.chart.WeightChartRenderer;
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
	private final DataSource dataSource;

	public ChartsPanel(DataSource dataSource) {
		super(new BorderLayout());
		this.dataSource = dataSource;
		var drawingPanel = new ChartRenderingPanel(new WeightChartRenderer(dataSource));
		this.add(drawingPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton drawButton = new JButton("Draw");
//		drawButton.addActionListener(e -> draw());
		buttonPanel.add(drawButton);
		this.add(buttonPanel, BorderLayout.NORTH);
	}
}
