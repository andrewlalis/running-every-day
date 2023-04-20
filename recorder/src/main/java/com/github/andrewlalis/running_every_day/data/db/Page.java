package com.github.andrewlalis.running_every_day.data.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public record Page<T>(
        List<T> items,
        Pagination pagination
) {
    public static <T> Page<T> fromResultSet(ResultSet rs, ResultSetMapper<T> mapper, Pagination pagination) throws SQLException {
        List<T> items = new ArrayList<>(pagination.size());
        while (rs.next() && items.size() < pagination.size()) {
            items.add(mapper.map(rs));
        }
        return new Page<>(items, pagination);
    }
}
