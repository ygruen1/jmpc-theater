package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jpmc.theater.MovieFactory.createMovie;
import static com.jpmc.theater.ShowingFactory.createShowing;

public class Theater {

    LocalDateProvider provider;
    private List<Showing> schedule;

    public Theater(LocalDateProvider provider) {
        this.provider = provider;

        Movie spiderMan = createMovie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, true);
        Movie turningRed = createMovie("Turning Red", Duration.ofMinutes(85), 11, false);
        Movie theBatMan = createMovie("The Batman", Duration.ofMinutes(95), 9, false);

        schedule = List.of(
                createShowing(turningRed, 1, provider.currentLocalDateAndTime(9, 0)),
                createShowing(spiderMan, 2, provider.currentLocalDateAndTime(11, 0)),
                createShowing(theBatMan, 3, provider.currentLocalDateAndTime(12, 50)),
                createShowing(turningRed, 4, provider.currentLocalDateAndTime(14, 30)),
                createShowing(spiderMan, 5, provider.currentLocalDateAndTime(16, 10)),
                createShowing(theBatMan, 6, provider.currentLocalDateAndTime(17, 50)),
                createShowing(turningRed, 7, provider.currentLocalDateAndTime(19, 30)),
                createShowing(spiderMan, 8, provider.currentLocalDateAndTime(21, 10)),
                createShowing(theBatMan, 9, provider.currentLocalDateAndTime(23, 0))
        );
    }

    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {

        if (sequence < 1 || sequence >= schedule.size()) {
            throw new IllegalStateException("Sequence must be within the proper range");
        }

        Showing showing = schedule.get(sequence - 1);
        return new Reservation(customer, showing, howManyTickets);
    }

    public void printSchedule() {
        System.out.println(provider.currentDate());
        printSimpleSchedule();
        printJSONschedule();
    }

    private void printJSONschedule() {
        System.out.println("===================================================");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            System.out.println(mapper.writeValueAsString(schedule));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void printSimpleSchedule() {
        System.out.println("===================================================");
        schedule.forEach(s ->
                System.out.println(s.getSequenceOfTheDay() + ": " + s.getStartTime() + " " + s.getMovie().getTitle() + " " + humanReadableFormat(s.getMovie().getRunningTime()) + " $" + s.getMovie().getBaseTicketPrice())
        );
    }

    public String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private String handlePlural(long value) {
        return value == 1 ? "" : "s";
    }

    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.getInstance());
        theater.printSchedule();

        System.out.println("===================================================");
        System.out.println("Thanks for this opportunity! ~Yehuda");
    }
}
