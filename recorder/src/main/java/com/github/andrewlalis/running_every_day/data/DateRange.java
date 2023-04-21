package com.github.andrewlalis.running_every_day.data;

import java.time.LocalDate;

public record DateRange(LocalDate start, LocalDate end) {
    public static DateRange after(LocalDate start) {
        return new DateRange(start, null);
    }

    public static DateRange before(LocalDate end) {
        return new DateRange(null, end);
    }

    public static DateRange unbounded() {
        return new DateRange(null, null);
    }

    public static DateRange lastNWeeks(int n) {
        return after(LocalDate.now().minusWeeks(n));
    }
}
