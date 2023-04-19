package com.github.andrewlalis.running_every_day.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
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
) {
    public BigDecimal distanceMeters() {
        return distanceKm.multiply(BigDecimal.valueOf(1000));
    }

    public int durationSeconds () {
        return (int) duration.getSeconds();
    }

    public String durationFormatted() {
        if (duration.toHoursPart() > 0) {
            return String.format("%d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        return String.format("%02d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
    }

    public BigDecimal weightGrams() {
        return weightKg.multiply(BigDecimal.valueOf(1000));
    }

    public static class Mapper implements ResultSetMapper<RunRecord> {
        @Override
        public RunRecord map(ResultSet rs) throws SQLException {
            long id = rs.getLong("id");
            String dateStr = rs.getString("date");
            String startTimeStr = rs.getString("start_time");
            int distanceMeters = rs.getInt("distance");
            int durationSeconds = rs.getInt("duration");
            int weightGrams = rs.getInt("weight");
            String comment = rs.getString("comment");
            return new RunRecord(
                    id,
                    LocalDate.parse(dateStr),
                    LocalTime.parse(startTimeStr),
                    BigDecimal.valueOf(distanceMeters, 3).divide(BigDecimal.valueOf(1000, 3), RoundingMode.UNNECESSARY),
                    Duration.ofSeconds(durationSeconds),
                    BigDecimal.valueOf(weightGrams, 3).divide(BigDecimal.valueOf(1000, 3), RoundingMode.UNNECESSARY),
                    comment
            );
        }
    }
}
