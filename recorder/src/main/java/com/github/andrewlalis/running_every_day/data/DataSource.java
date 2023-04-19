package com.github.andrewlalis.running_every_day.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource implements AutoCloseable {
    private final Connection conn;

    public DataSource(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
        this.initSchemaIfNeeded();
    }

    public void close() throws SQLException {
        this.conn.close();
    }

    private void initSchemaIfNeeded() throws SQLException {
        boolean shouldInitSchema;
        try (
                var stmt = this.conn.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name='run'");
                var rs = stmt.executeQuery()
        ) {
            shouldInitSchema = !rs.next();
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
