package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.DataSource;

import javax.swing.*;
import java.awt.*;

public class RecorderAppWindow extends JFrame {
    public RecorderAppWindow(DataSource dataSource) {
        super("Run-Recorder");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // TODO: Build UI
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
