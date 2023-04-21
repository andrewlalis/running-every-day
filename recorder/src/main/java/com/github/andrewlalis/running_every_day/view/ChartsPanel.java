package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.view.chart.ChartRenderingPanel;
import com.github.andrewlalis.running_every_day.view.chart.DateRangePanel;
import com.github.andrewlalis.running_every_day.view.chart.DateSeriesCharts;
import com.github.andrewlalis.running_every_day.view.chart.ExportChartImageDialog;
import com.github.andrewlalis.running_every_day.view.chart.menu.DateSeriesChartMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class ChartsPanel extends JPanel {
	private final DataSource dataSource;

	private final ChartRenderingPanel chartRenderingPanel;

	public ChartsPanel(DataSource dataSource) {
		super(new BorderLayout());
		this.dataSource = dataSource;
		this.chartRenderingPanel = new ChartRenderingPanel();
		this.add(chartRenderingPanel, BorderLayout.CENTER);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton exportButton = new JButton("Export to Image");
		exportButton.addActionListener(e -> {
			var dialog = new ExportChartImageDialog(this, chartRenderingPanel.getRenderer());
			dialog.setVisible(true);
		});
		topPanel.add(exportButton);
		this.add(topPanel, BorderLayout.NORTH);

		JPanel chartMenu = new JPanel();
		chartMenu.setLayout(new BoxLayout(chartMenu, BoxLayout.PAGE_AXIS));

		chartMenu.add(new DateSeriesChartMenuItem(
				"Weight",
				"A chart that depicts weight change over time.",
				chartRenderingPanel,
				dataSource,
				DateRange.lastNWeeks(2),
				DateSeriesCharts::weight
		));
		chartMenu.add(new DateSeriesChartMenuItem(
				"Pace",
				"A chart that depicts average pace in minutes per kilometer.",
				chartRenderingPanel,
				dataSource,
				DateRange.lastNWeeks(2),
				DateSeriesCharts::pace
		));
		chartMenu.add(new DateSeriesChartMenuItem(
				"Total Distance",
				"A chart showing the total distance accumulated over time, in kilometers.",
				chartRenderingPanel,
				dataSource,
				DateRange.lastNWeeks(4),
				DateSeriesCharts::totalDistance
		));

		chartMenu.add(Box.createVerticalGlue());

		var menuScroll = new JScrollPane(chartMenu, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScroll.getVerticalScrollBar().setUnitIncrement(10);
		this.add(menuScroll, BorderLayout.EAST);
	}
}
