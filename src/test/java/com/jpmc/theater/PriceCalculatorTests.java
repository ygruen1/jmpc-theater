package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceCalculatorTests {

    LocalDateProvider ldp = LocalDateProvider.getInstance();
    Movie movie;
    Showing showing;

    private void arrange(double ticketPrice, boolean isSpecialMovie, int sequence, int hour, int minute) {
        movie = MovieFactory.createMovie("Spider-Man: No Way Home", Duration.ofMinutes(90), ticketPrice, isSpecialMovie);
        showing = ShowingFactory.createShowing(movie, sequence, ldp.currentLocalDateAndTime(hour, minute));
    }


    @Test
    void whenSpecialMovie_thenPriceIsDiscountedBy20Percent() {
        arrange(12.5, true, 5, 9, 0);
        assertEquals(10, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieIsDuringFirstShowing_thenPriceIsDiscountedBy3Dollars() {
        arrange(12.5, false, 1, 9, 0);
        assertEquals(9.5, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieIsDuringSecondShowing_thenPriceIsDiscountedBy2Dollars() {
        arrange(12.5, false, 2, 9, 0);
        assertEquals(10.5, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieIsDuring7thShowing_thenPriceIsDiscountedBy1Dollar() {
        arrange(12.5, false, 7, 9, 0);
        assertEquals(11.5, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieIsBetween11And4_thenPriceIsDiscountedBy25Percent() {
        arrange(12.5, false, 5, 12, 50);
        assertEquals(9.38, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieHasMultipleDiscounts_thenPriceIsDiscountedByLargestDiscountOnly() {
        //Movie has 20% (i.e. $2) discount for special and $3 discount for 1st showing
        arrange(10, true, 1, 9, 0);
        assertEquals(7, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMovieHasNoDiscounts_thenPriceIsntDiscounted() {
        arrange(12.5, false, 5, 9, 0);
        assertEquals(12.5, PriceCalculator.calculateCost(showing, 1));
    }

    @Test
    void whenMultipleTickets_calculateTotalWithDiscounts() {
        //Movie has 3 discounts (special, second showing, & between 11 and 4)
        //so it gets the largest discount of 25% off each ticket

        Theater theater = new Theater(LocalDateProvider.getInstance());
        Customer john = new Customer("John Doe", "id-12345");
        Reservation reservation = theater.reserve(john, 2, 4);
        assertEquals(37.5, reservation.calculateReservationCost());
    }

}
