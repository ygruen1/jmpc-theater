package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TheaterTests {

    @Test
    void whenSequenceDoesntExist_thenThrowError() {
        Theater theater = new Theater(LocalDateProvider.getInstance());
        Customer c = new Customer("John Smith", "jsmith");
        assertThrows(IllegalStateException.class, () -> theater.reserve(c, 0, 1));
        assertThrows(IllegalStateException.class, () -> theater.reserve(c, 10, 1));
    }

    @Test
    void printMovieSchedule() {
        Theater theater = new Theater(LocalDateProvider.getInstance());
        theater.printSchedule();
    }
}
