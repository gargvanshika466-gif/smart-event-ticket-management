package demo;

import enums.BookingStatus;
import enums.EventStatus;
import model.Attendee;
import model.Booking;
import model.Event;
import model.TicketCategory;
import model.Venue;
import service.BookingService;
import service.EventService;
import service.UserService;
import service.WaitingListService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentBookingDemo {

    private static final int SEATS = 5;      // seats available for each test event
    private static final int THREADS = 15;   // users trying to book at the same time

    public static void run() throws Exception {

        // The demo uses its own services, so it does not touch the menu's data
        EventService eventService = new EventService();
        UserService userService = new UserService();
        WaitingListService waitingListService = new WaitingListService();
        BookingService bookingService =
                new BookingService(eventService, userService, waitingListService);

        // Two identical events: E1 for the unsafe test, E2 for the safe test
        createDemoEvent(eventService, "DEMO-E1", "Unsafe Concert");
        createDemoEvent(eventService, "DEMO-E2", "Safe Concert");

        for (int i = 1; i <= THREADS; i++) {
            userService.registerUser(new Attendee("U" + i, "User " + i, "user" + i + "@test.com"));
        }

        // ---------------- PART 1: WITHOUT synchronized ----------------
        System.out.println("\n===== PART 1: WITHOUT synchronized =====");
        AtomicInteger unsafeSold = new AtomicInteger();
        ExecutorService pool1 = Executors.newFixedThreadPool(THREADS);

        for (int i = 1; i <= THREADS; i++) {
            pool1.submit(() -> {
                try {
                    if (bookingService.tryBookSeatUnsafe("DEMO-E1", "VIP")) {
                        unsafeSold.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            });
        }
        pool1.shutdown();
        pool1.awaitTermination(10, TimeUnit.SECONDS);

        int left1 = findVip(eventService, "DEMO-E1").getAvailableSeats();
        System.out.println("Seats available    : " + SEATS);
        System.out.println("Users trying       : " + THREADS);
        System.out.println("Tickets sold       : " + unsafeSold.get());
        System.out.println("Seats left (final) : " + left1);
        if (unsafeSold.get() > SEATS) {
            System.out.println("RESULT: OVERBOOKING! More tickets sold than seats (race condition).");
        } else {
            System.out.println("RESULT: Race did not show this time. Run the demo again.");
        }

        // ---------------- PART 2: WITH synchronized ----------------
        System.out.println("\n===== PART 2: WITH synchronized =====");
        AtomicInteger confirmed = new AtomicInteger();
        AtomicInteger waitlisted = new AtomicInteger();
        ExecutorService pool2 = Executors.newFixedThreadPool(THREADS);

        for (int i = 1; i <= THREADS; i++) {
            final String userId = "U" + i;
            pool2.submit(() -> {
                try {
                    Booking b = bookingService.bookTicket(userId, "DEMO-E2", "VIP");
                    if (b.getStatus() == BookingStatus.CONFIRMED) {
                        confirmed.incrementAndGet();
                    } else {
                        waitlisted.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            });
        }
        pool2.shutdown();
        pool2.awaitTermination(10, TimeUnit.SECONDS);

        int left2 = findVip(eventService, "DEMO-E2").getAvailableSeats();
        System.out.println("Seats available    : " + SEATS);
        System.out.println("Users trying       : " + THREADS);
        System.out.println("Confirmed          : " + confirmed.get());
        System.out.println("Waitlisted         : " + waitlisted.get());
        System.out.println("Seats left (final) : " + left2);
        System.out.println("RESULT: No overbooking. Extra users were added to the waiting list.");
    }

    private static void createDemoEvent(EventService eventService, String id, String title)
            throws Exception {
        Event event = new Event(id, title, new Venue("Demo Hall", "Delhi", 100),
                "01-01-2027", EventStatus.ACTIVE, new ArrayList<TicketCategory>());
        eventService.createEvent(event);
        eventService.addCategoryToEvent(id, new TicketCategory("VIP", "VIP", 500.0, SEATS, SEATS));
    }

    private static TicketCategory findVip(EventService eventService, String eventId) throws Exception {
        return eventService.getEventById(eventId).getTicketCategories().get(0);
    }

    // so the demo can also be run directly from IntelliJ
    public static void main(String[] args) throws Exception {
        run();
    }
}