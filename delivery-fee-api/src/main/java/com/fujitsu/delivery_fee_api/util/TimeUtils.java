package com.fujitsu.delivery_fee_api.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeUtils {

    private static final String TALLINN_ZONE = "Europe/Tallinn";

    public int convertToEpochSeconds(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.of(TALLINN_ZONE);
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        return Math.toIntExact(zonedDateTime.toEpochSecond());
    }

    public static LocalDateTime getCurrentDateTimeIfNull(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime).orElseGet(() -> {
            log.info("No dateTime provided. Using current time: {}", LocalDateTime.now());
            return LocalDateTime.now();
        });
    }
    
}
