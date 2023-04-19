package com.github.andrewlalis.running_every_day.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private final Connection conn;

    public DataSource(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
        this.initSchemaIfNeeded();
    }

    public RunRecordRepository runRecords() {
        return new RunRecordRepository(this.conn);
    }

    public void close() throws SQLException {
        this.conn.close();
    }

    /**
     * Checks for any missing tables and initializes the database schema with
     * them if so.
     * @throws SQLException If an SQL error occurs.
     */
    private void initSchemaIfNeeded() throws SQLException {
        boolean shouldInitSchema = false;
        final String[] tableNames = {"run"};
        try (var stmt = this.conn.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
            for (var name : tableNames) {
                stmt.setString(1, name);
                try (var rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        shouldInitSchema = true;
                        break;
                    }
                }
            }
        }
        if (shouldInitSchema) {
            try (var stmt = this.conn.createStatement()) {
                stmt.execute(readResource("schema.sql"));
            }
        }
    }

    private static String readResource(String name) {
        try (var in = DataSource.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) {
                throw new RuntimeException("Missing resource: " + name);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
