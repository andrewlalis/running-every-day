package com.github.andrewlalis.running_every_day.data;

import com.github.andrewlalis.running_every_day.data.db.Page;
import com.github.andrewlalis.running_every_day.data.db.Pagination;
import com.github.andrewlalis.running_every_day.data.db.Queries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for interacting with run records in the database.
 * @param conn The database connection.
 */
public record RunRecordRepository(Connection conn) {
    public Page<RunRecord> findAll(Pagination pagination) throws SQLException {
        String query = "SELECT * FROM run ORDER BY date ASC";
        return pagination.execute(conn, query, new RunRecord.Mapper());
    }

    public List<RunRecord> findAllByQuery(String query) throws SQLException {
        List<RunRecord> items = new ArrayList<>();
        var mapper = new RunRecord.Mapper();
        try (
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                items.add(mapper.map(rs));
            }
        }
        return items;
    }

    public void delete(long id) throws SQLException {
        try (var stmt = conn.prepareStatement("DELETE FROM run WHERE id = ?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public RunRecord save(RunRecord runRecord) throws SQLException {
        String query = "INSERT INTO run (date, start_time, distance, duration, weight, comment) VALUES (?, ?, ?, ?, ?, ?)";
        try (var stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, runRecord.date().toString());
            stmt.setString(2, runRecord.startTime().toString());
            stmt.setInt(3, runRecord.distanceMeters().intValue());
            stmt.setInt(4, runRecord.durationSeconds());
            stmt.setInt(5, runRecord.weightGrams().intValue());
            stmt.setString(6, runRecord.comment());
            stmt.executeUpdate();
            try (var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return new RunRecord(
                            id,
                            runRecord.date(),
                            runRecord.startTime(),
                            runRecord.distanceKm(),
                            runRecord.duration(),
                            runRecord.weightKg(),
                            runRecord.comment()
                    );
                } else {
                    throw new SQLException("Missing generated primary key for record.");
                }
            }
        }
    }

    public long pageCount(int pageSize) throws SQLException {
        return Queries.pageCount(conn, "SELECT COUNT(id) FROM run", pageSize);
    }

    // Aggregate stats:

    public BigDecimal getTotalDistanceKm() throws SQLException {
        long totalDistanceMeters = Queries.getLong(conn, "SELECT SUM(distance) FROM run");
        return BigDecimal.valueOf(totalDistanceMeters, 3)
                .divide(BigDecimal.valueOf(1000, 3), RoundingMode.UNNECESSARY);
    }

    public Duration getTotalDuration() throws SQLException {
        long totalDurationSeconds = Queries.getLong(conn, "SELECT SUM(duration) FROM run");
        return Duration.ofSeconds(totalDurationSeconds);
    }
}
