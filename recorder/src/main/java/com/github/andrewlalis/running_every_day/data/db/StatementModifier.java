package com.github.andrewlalis.running_every_day.data.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementModifier {
    void apply(PreparedStatement stmt) throws SQLException;
}
