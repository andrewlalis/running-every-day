package com.github.andrewlalis.running_every_day.data.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Queries {
    private Queries() {}

    public static <T> Optional<T> findOne(
            Connection c,
            String query,
            StatementModifier modifier,
            ResultSetMapper<T> mapper
    ) throws SQLException {
        try (var stmt = c.prepareStatement(query)) {
            modifier.apply(stmt);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper.map(rs));
                }
                return Optional.empty();
            }
        }
    }

    public static <T> Optional<T> findOne(Connection c, String query, ResultSetMapper<T> mapper) throws SQLException {
        try (var stmt = c.prepareStatement(query)) {
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper.map(rs));
                }
                return Optional.empty();
            }
        }
    }

    public static <T> T getOne(Connection c, String query, ResultSetMapper<T> mapper) throws SQLException {
        return findOne(c, query, mapper).orElseThrow(() -> new SQLException("Missing required SQL result."));
    }

    public static long getLong(Connection c, String query) throws SQLException {
        return getOne(c, query, rs -> rs.getLong(1));
    }

    public static String getString(Connection c, String query) throws SQLException {
        return getOne(c, query, rs -> rs.getString(1));
    }

    public static long count(Connection c, String query) throws SQLException {
        try (var stmt = c.prepareStatement(query); var rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("Result set for count query is empty.");
            }
        }
    }

    public static long pageCount(Connection c, String query, int pageSize) throws SQLException {
        long recordCount = count(c, query);
        long pageCount = recordCount / pageSize;
        if (recordCount % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }

    public static <T> List<T> findAll(Connection c, String query, ResultSetMapper<T> mapper) throws SQLException {
        try (var stmt = c.prepareStatement(query)) {
            try (var rs = stmt.executeQuery()) {
                List<T> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(mapper.map(rs));
                }
                return items;
            }
        }
    }

    public static int update(Connection c, String query) throws SQLException {
        try (var stmt = c.prepareStatement(query)) {
            return stmt.executeUpdate();
        }
    }
}
