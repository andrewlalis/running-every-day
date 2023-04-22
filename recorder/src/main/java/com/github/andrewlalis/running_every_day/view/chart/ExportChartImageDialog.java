package com.github.andrewlalis.running_every_day.view.chart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class ExportChartImageDialog extends JDialog {
    private final ChartRenderer chartRenderer;

    private final SpinnerNumberModel widthSpinnerModel = new SpinnerNumberModel(1920, 10, 10000, 1);
    private final SpinnerNumberModel heightSpinnerModel = new SpinnerNumberModel(1080, 10, 10000, 1);
    private final SpinnerNumberModel textScaleSpinnerModel = new SpinnerNumberModel(1.0, 0.01, 25.0, 0.01);
    private final JTextField filePathField = new JTextField();
    private Path currentFilePath = Path.of(".").toAbsolutePath().resolve("chart.png");

    public ExportChartImageDialog(Component parent, ChartRenderer chartRenderer) {
        super(SwingUtilities.getWindowAncestor(parent), "Export Chart to Image", ModalityType.APPLICATION_MODAL);
        this.chartRenderer = chartRenderer;

        this.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0;
        c.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Width (px)"), c);
        c.gridy = 1;
        formPanel.add(new JLabel("Height (px)"), c);
        c.gridy = 2;
        formPanel.add(new JLabel("Text Scale"), c);
        c.gridy = 3;
        formPanel.add(new JLabel("File"), c);
        c.gridy = 4;
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(e -> browseForFilePath());
        formPanel.add(selectFileButton, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JSpinner widthSpinner = new JSpinner(widthSpinnerModel);
        widthSpinner.setLocale(Locale.US);
        formPanel.add(widthSpinner, c);
        c.gridy = 1;
        JSpinner heightSpinner = new JSpinner(heightSpinnerModel);
        heightSpinner.setLocale(Locale.US);
        formPanel.add(heightSpinner, c);
        c.gridy = 2;
        JSpinner textScaleSpinner = new JSpinner(textScaleSpinnerModel);
        textScaleSpinner.setLocale(Locale.US);
        formPanel.add(textScaleSpinner, c);

        c.gridy = 3;
        filePathField.setEditable(false);
        filePathField.setText(currentFilePath.toAbsolutePath().toString());
        filePathField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                browseForFilePath();
            }
        });
        formPanel.add(filePathField, c);
        this.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> {
            exportChart();
        });
        buttonPanel.add(exportButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());
        buttonPanel.add(cancelButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(600, 300));
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    private void browseForFilePath() {
        JFileChooser fileChooser = new JFileChooser(currentFilePath.toFile());
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showDialog(this, "Save");
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFilePath = fileChooser.getSelectedFile().toPath();
            filePathField.setText(currentFilePath.toAbsolutePath().toString());
        }
    }

    private void exportChart() {
        if (Files.exists(currentFilePath)) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "The file " + currentFilePath + " already exists.\nAre you sure you want to overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result != JOptionPane.OK_OPTION) return;
        }
        int width = (int) widthSpinnerModel.getValue();
        int height = (int) heightSpinnerModel.getValue();
        double textScale = (Double) textScaleSpinnerModel.getValue();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        chartRenderer.render(img.createGraphics(), new Rectangle2D.Float(0, 0, width, height), (float) textScale);
        try {
            ImageIO.write(img, "png", currentFilePath.toFile());
            JOptionPane.showMessageDialog(
                    this,
                    "Export complete!",
                    "Export Complete",
                    JOptionPane.PLAIN_MESSAGE
            );
            this.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred:\n" + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
