package com.github.andrewlalis.running_every_day.data.db;

import com.github.andrewlalis.running_every_day.Resources;
import com.github.andrewlalis.running_every_day.data.RunRecordRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A single object that serves as the application's data source.
 */
public class DataSource {
    private final Connection conn;

    public DataSource(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
        this.initSchemaIfNeeded();
    }

    public RunRecordRepository runRecords() {
        return new RunRecordRepository(this.conn);
    }

    public Connection conn() {
        return conn;
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
                String query = Resources.readResourceAsString("schema.sql");
                stmt.execute(query);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
