package com.jpmc.theater;

import java.time.Duration;

public class MovieFactory {

    public static Movie createMovie(String name, Duration duration, double ticketPrice, boolean isSpecialMovie) {
        return new Movie(name, duration, ticketPrice, isSpecialMovie);
    }

}
