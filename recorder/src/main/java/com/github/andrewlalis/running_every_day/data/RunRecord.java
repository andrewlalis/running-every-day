package com.github.andrewlalis.running_every_day.data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * An immutable record that represents an entry in the `run` table.
 * @param id The unique id of the record.
 * @param date The date of the record.
 * @param startTime The time at which the record started.
 * @param distanceKm The distance in kilometers.
 * @param duration The duration of the run.
 * @param weightKg The body-weight recorded for the run.
 * @param comment Any comments for the run.
 */
public record RunRecord(
        long id,
        LocalDate date,
        LocalTime startTime,
        BigDecimal distanceKm,
        Duration duration,
        BigDecimal weightKg,
        String comment
) {}
