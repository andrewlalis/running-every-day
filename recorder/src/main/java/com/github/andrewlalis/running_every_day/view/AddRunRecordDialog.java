package com.github.andrewlalis.running_every_day.view;

import com.github.andrewlalis.running_every_day.data.db.DataSource;
import com.github.andrewlalis.running_every_day.data.RunRecord;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AddRunRecordDialog extends JDialog {
	private final DataSource dataSource;

	private final JTextField dateField;
	private final JTextField timeField;
	private final JTextField distanceField;
	private final JTextField durationField;
	private final JTextField weightField;
	private final JTextArea commentField;

	private boolean added = false;

	public AddRunRecordDialog(Window owner, DataSource dataSource) {
		super(owner, "Add a Record", ModalityType.APPLICATION_MODAL);

		this.dataSource = dataSource;

		this.dateField = new JTextField(LocalDate.now().toString(), 0);
		this.timeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), 0);
		this.distanceField = new JTextField("5.0", 0);
		this.durationField = new JTextField("00:00:00", 0);
		this.weightField = new JTextField("85.0", 0);
		this.commentField = new JTextArea(3, 0);

		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		contentPanel.add(buildFieldsPanel());
		mainPanel.add(contentPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton addButton = new JButton("Add");
		addButton.addActionListener(e -> onAdd());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> this.dispose());
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.setContentPane(mainPanel);

		this.setPreferredSize(new Dimension(400, 300));
		this.pack();
		this.setLocationRelativeTo(owner);
	}

	public boolean isAdded() {
		return added;
	}

	private Container buildFieldsPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		var c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		String[] labels = new String[]{"Date", "Start Time", "Distance (Km)", "Duration (HH:MM:SS)", "Weight (Kg)", "Comments"};
		for (var label : labels) {
			panel.add(new JLabel(label), c);
			c.gridy++;
		}
		Component[] fields = new Component[]{dateField, timeField, distanceField, durationField, weightField, commentField};
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		for (var field : fields) {
			if (field instanceof JTextArea) {
				c.fill = GridBagConstraints.BOTH;
			} else {
				c.fill = GridBagConstraints.HORIZONTAL;
			}
			panel.add(field, c);
			c.gridy++;
		}
		return panel;
	}

	private List<String> validateForm() {
		List<String> errorMessages = new ArrayList<>();
		LocalDate date = null;
		try {
			date = LocalDate.parse(dateField.getText());
			if (date.isAfter(LocalDate.now())) {
				errorMessages.add("Cannot add a run for a date in the future.");
			}
		} catch (DateTimeParseException e) {
			errorMessages.add("Invalid date format. Should be YYYY-MM-DD.");
		}
		try {
			var time = LocalTime.parse(timeField.getText());
			if (date != null && date.atTime(time).isAfter(LocalDateTime.now())) {
				errorMessages.add("Cannot add a run for a time in the future.");
			}
		} catch (DateTimeParseException e) {
			errorMessages.add("Invalid start time format. Should be HH:MM.");
		}

		try {
			BigDecimal distanceKm = new BigDecimal(distanceField.getText());
			if (distanceKm.compareTo(BigDecimal.ZERO) < 0 || distanceKm.scale() > 3) {
				errorMessages.add("Invalid distance.");
			}
		} catch (NumberFormatException e) {
			errorMessages.add("Invalid or missing distance.");
		}

		try {
			Duration duration = parseDuration(durationField.getText());
			if (duration.isNegative() || duration.isZero()) {
				errorMessages.add("Duration must be positive.");
			}
		} catch (IllegalArgumentException e) {
			errorMessages.add(e.getMessage());
		}

		try {
			BigDecimal weightKg = new BigDecimal(weightField.getText());
			if (weightKg.compareTo(BigDecimal.ZERO) < 0 || weightKg.scale() > 3) {
				errorMessages.add("Invalid weight.");
			}
		} catch (NumberFormatException e) {
			errorMessages.add("Invalid or missing weight.");
		}

		if (commentField.getText().isBlank()) {
			errorMessages.add("Comments should not be empty.");
		}

		return errorMessages;
	}

	private void onAdd() {
		var messages = validateForm();
		if (!messages.isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"Form validation failed:\n" + String.join("\n", messages),
					"Validation Failed",
					JOptionPane.WARNING_MESSAGE
			);
		} else {
			try {
				this.dataSource.runRecords().save(new RunRecord(
						0,
						LocalDate.parse(dateField.getText()),
						LocalTime.parse(timeField.getText()),
						new BigDecimal(distanceField.getText()),
						parseDuration(durationField.getText()),
						new BigDecimal(weightField.getText()),
						commentField.getText().strip()
				));
				this.added = true;
				this.dispose();
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						this,
						"An SQL Exception occurred: " + e.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE
				);
			}

		}
	}

	private static Duration parseDuration(String s) {
		String[] parts = s.strip().split(":");
		if (parts.length < 2 || parts.length > 3) {
			throw new IllegalArgumentException("Invalid or missing duration.");
		}
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		if (parts.length == 3) {
			hours = Integer.parseInt(parts[0]);
			minutes = Integer.parseInt(parts[1]);
			seconds = Integer.parseInt(parts[2]);
		} else {
			minutes = Integer.parseInt(parts[0]);
			seconds = Integer.parseInt(parts[1]);
		}
		return Duration.ofSeconds(hours * 60L * 60 + minutes * 60L + seconds);
	}
}
