package com.github.andrewlalis.running_every_day.data.db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
