package com.jpmc.theater;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceCalculator {

    private final static LocalDateProvider ldp = LocalDateProvider.getInstance();

    public static double calculateCost(Showing showing, int audienceCount) {

        List<Double> discountsList = new ArrayList<>();
        double baseTicketPrice = showing.getMovie().getBaseTicketPrice();

        discountsList.add(calculateSpecialDiscount(baseTicketPrice, showing.getMovie().getSpecialMovie()));
        discountsList.add(calculateSequenceDiscount(showing));
        discountsList.add(calculateTimeFrameDiscount(baseTicketPrice, showing));

        discountsList.sort(Comparator.reverseOrder());

        //only deduct the largest discount
        double calculatedCost = (baseTicketPrice - discountsList.get(0)) * audienceCount;

        return BigDecimal.valueOf(calculatedCost).setScale(2, RoundingMode.HALF_UP).doubleValue(); //Rounded to 2 decimal places
    }

    private static double calculateTimeFrameDiscount(double baseTicketPrice, Showing showing) {

        LocalDateTime showingStartTime = showing.getStartTime();
        LocalDateTime discountStartTime = ldp.currentLocalDateAndTime(10, 59, 59); // To include 11am
        LocalDateTime discountEndTime = ldp.currentLocalDateAndTime(16, 0, 1); //To include 4pm

        double timeFrameDiscount = 0;
        if (showingStartTime.isAfter(discountStartTime) && showingStartTime.isBefore(discountEndTime)) {
            timeFrameDiscount = baseTicketPrice * .25;
        }

        return timeFrameDiscount;
    }

    private static double calculateSequenceDiscount(Showing showing) {

        double sequenceDiscount = 0;
        if (showing.isSequence(1)) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (showing.isSequence(2)) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        } else if (showing.isSequence(7)) {
            sequenceDiscount = 1; // $1 discount for 7th show
        }

        return sequenceDiscount;
    }

    private static double calculateSpecialDiscount(double baseTicketPrice, boolean isSpecialMovie) {
        return isSpecialMovie ? baseTicketPrice * 0.2 : 0;
    }

}
