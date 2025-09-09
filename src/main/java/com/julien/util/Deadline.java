package com.julien.util;

import java.time.Duration;


public final class Deadline {
    private final long endNanos;

    public Deadline(Duration budget) {
        long now = System.nanoTime();
        this.endNanos = now + budget.toNanos();
    }

    public boolean isExpired() {
        return System.nanoTime() > endNanos;
    }
}
