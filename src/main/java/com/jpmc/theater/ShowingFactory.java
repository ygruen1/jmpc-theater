package com.jpmc.theater;

import java.time.LocalDateTime;

public class ShowingFactory {

    public static Showing createShowing(Movie movie, int sequence, LocalDateTime ldt) {
        return new Showing(movie, sequence, ldt);
    }

}
