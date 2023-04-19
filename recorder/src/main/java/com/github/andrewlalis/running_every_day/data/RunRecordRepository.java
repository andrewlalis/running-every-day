package com.github.andrewlalis.running_every_day.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public long countAll() throws SQLException {
        String query = "SELECT COUNT(id) FROM run";
        try (
                var stmt = conn.prepareStatement(query);
                var rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Missing count.");
            }
        }
    }

    public long pageCount(int size) throws SQLException {
        long recordCount = countAll();
        return (recordCount / size) + (recordCount % size != 0 ? 1 : 0);
    }
}
