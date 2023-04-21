package com.github.andrewlalis.running_every_day.view.chart.menu;

import com.github.andrewlalis.running_every_day.view.chart.ChartRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ChartMenuItem extends JPanel {
    private final JButton viewButton = new JButton("View");
    protected final Consumer<ChartRenderer> rendererConsumer;

    public ChartMenuItem(String name, String description, Consumer<ChartRenderer> rendererConsumer) {
        this.rendererConsumer = rendererConsumer;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(name);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(titleLabel);
        this.add(headerPanel);

        JTextArea descriptionArea = new JTextArea(0, 0);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setText(description);
        this.add(descriptionArea);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.add(viewButton);
        this.add(buttonsPanel);
    }

    public void setupViewAction(ActionListener l) {
        viewButton.addActionListener(l);
    }
}
