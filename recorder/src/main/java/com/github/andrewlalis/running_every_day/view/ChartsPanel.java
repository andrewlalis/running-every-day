package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DateRange;
import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.view.chart.ChartRenderingPanel;
import com.github.andrewlalis.running_every_day.view.chart.DateSeriesCharts;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ChartsPanel extends JPanel {
	private final DataSource dataSource;

	private final ChartRenderingPanel chartRenderingPanel;

	public ChartsPanel(DataSource dataSource) {
		super(new BorderLayout());
		this.dataSource = dataSource;
		this.chartRenderingPanel = new ChartRenderingPanel();
		this.add(chartRenderingPanel, BorderLayout.CENTER);

		JPanel chartMenu = new JPanel();
		chartMenu.setLayout(new BoxLayout(chartMenu, BoxLayout.PAGE_AXIS));
		chartMenu.add(buildWeightChartMenuPanel());
		chartMenu.add(buildPaceChartMenuPanel());
		var menuScroll = new JScrollPane(chartMenu, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		menuScroll.setPreferredSize(new Dimension(300, -1));
		menuScroll.getVerticalScrollBar().setUnitIncrement(10);
		this.add(menuScroll, BorderLayout.EAST);
	}

	private JPanel buildBasicChartMenuItem(String name, String description) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createRaisedBevelBorder());

		JLabel titleLabel = new JLabel(name);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(titleLabel);

		JTextArea descriptionArea = new JTextArea();
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setEditable(false);
		descriptionArea.setText(description);
		panel.add(descriptionArea);

		return panel;
	}

	private JPanel buildWeightChartMenuPanel() {
		JPanel panel = buildBasicChartMenuItem("Weight", "A chart that depicts weight change over time.");

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton viewButton = new JButton("View");
		viewButton.addActionListener(e -> chartRenderingPanel.setRenderer(DateSeriesCharts.weight(dataSource, DateRange.unbounded())));
		buttonsPanel.add(viewButton);
		panel.add(buttonsPanel);
		return panel;
	}

	private JPanel buildPaceChartMenuPanel() {
		JPanel panel = buildBasicChartMenuItem("Average Pace", "A chart that depicts the average pace of each run.");

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton viewButton = new JButton("View");
		viewButton.addActionListener(e -> chartRenderingPanel.setRenderer(DateSeriesCharts.pace(dataSource, DateRange.unbounded())));
		buttonsPanel.add(viewButton);
		panel.add(buttonsPanel);
		return panel;
	}
}
