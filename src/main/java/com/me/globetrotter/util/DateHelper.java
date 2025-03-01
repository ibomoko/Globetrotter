package com.me.globetrotter.util;

import java.time.Instant;
import java.util.Date;

public class DateHelper {
    public static Date getUtcNow() {
        return Date.from(Instant.now());
    }
}
