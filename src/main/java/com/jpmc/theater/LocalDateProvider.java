package com.jpmc.theater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateProvider {

    private static LocalDateProvider instance = null;
    private final static Object lock = new Object();

    private LocalDateProvider() {
    }

    public static LocalDateProvider getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LocalDateProvider();
                }
            }
        }
        return instance;
    }

    public LocalDate currentDate() {
        return LocalDate.now();
    }

    public LocalTime localTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    public LocalDateTime currentLocalDateAndTime(int hour, int minute, int second) {
        return LocalDateTime.of(currentDate(), localTime(hour, minute, second));
    }

    public LocalDateTime currentLocalDateAndTime(int hour, int minute) {
        return currentLocalDateAndTime(hour, minute, 0);
    }
}
