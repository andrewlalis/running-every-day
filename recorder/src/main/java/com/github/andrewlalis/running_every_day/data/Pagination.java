package com.github.andrewlalis.running_every_day.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public record Pagination(int page, int size, List<Sort> sorts) {
    public record Sort(String property, SortDir direction, NullsDir nullsDirection) {
        public Sort(String property, SortDir direction) {
            this(property, direction, NullsDir.FIRST);
        }

        public Sort(String property) {
            this(property, SortDir.ASC);
        }
    }
    public enum SortDir {ASC, DESC}
    public enum NullsDir {FIRST, LAST}

    public Pagination(int page, int size) {
        this(page, size, Collections.emptyList());
    }

    public Pagination nextPage() {
        return new Pagination(this.page + 1, this.size, this.sorts);
    }

    public Pagination previousPage() {
        return new Pagination(this.page - 1, this.size, this.sorts);
    }

    public String toQuerySyntax() {
        StringBuilder sb = new StringBuilder(256);
        List<String> orderingTerms = this.sorts.stream()
                .map(s -> s.property + ' ' + s.direction.name())
                .toList();
        if (!orderingTerms.isEmpty()) {
            sb.append(String.join(",", orderingTerms)).append(' ');
        }
        sb.append("LIMIT ").append(this.size).append(" OFFSET ").append(this.page * this.size);
        return sb.toString();
    }

    public <T> Page<T> execute(Connection conn, String baseQuery, ResultSetMapper<T> mapper) throws SQLException {
        String query = baseQuery + ' ' + this.toQuerySyntax();
        try (var stmt = conn.prepareStatement(query)) {
            return Page.fromResultSet(stmt.executeQuery(), mapper, this);
        }
    }
}
