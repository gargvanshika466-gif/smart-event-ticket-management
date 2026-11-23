package service;

import enums.BookingStatus;
import enums.EventStatus;
import exception.DuplicateBookingException;
import exception.EventNotFoundException;
import exception.InvalidInputException;
import exception.SeatNotAvailableException;
import model.Booking;
import model.Event;
import model.TicketCategory;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingService {

    private HashMap<String, Booking> bookings = new HashMap<>();
    private EventService eventService;
    private UserService userService;
    private WaitingListService waitingListService;

    private int bookingCounter = 1;
    private boolean waitlistEnabled = true;

    public BookingService(EventService eventService, UserService userService,
                          WaitingListService waitingListService) {
        this.eventService = eventService;
        this.userService = userService;
        this.waitingListService = waitingListService;
    }

    public void setWaitlistEnabled(boolean enabled) {
        this.waitlistEnabled = enabled;
    }

    // ======================= BOOK =======================
    public synchronized Booking bookTicket(String userId, String eventId, String categoryId)
            throws InvalidInputException, EventNotFoundException,
            DuplicateBookingException, SeatNotAvailableException {

        // 1. validate input
        if (isEmpty(userId) || isEmpty(eventId) || isEmpty(categoryId)) {
            throw new InvalidInputException("User, event and category ids cannot be empty");
        }

        // 2. user, event and category must exist
        User user = userService.getUserById(userId);            // throws InvalidInputException
        Event event = eventService.getEventById(eventId);       // throws EventNotFoundException
        if (event.getStatus() == EventStatus.CANCELLED || event.getStatus() == EventStatus.COMPLETED) {
            throw new InvalidInputException("Event is not active: " + eventId);
        }
        TicketCategory category = findCategory(event, categoryId);
        if (category == null) {
            throw new InvalidInputException("Category not found: " + categoryId);
        }

        // 3. duplicate check (cancelled bookings do not count)
        for (Booking b : bookings.values()) {
            if (b.getUser().getId().equals(userId)
                    && b.getEvent().getId().equals(eventId)
                    && b.getStatus() != BookingStatus.CANCELLED) {
                throw new DuplicateBookingException(
                        "User " + userId + " already has a booking for event " + eventId);
            }
        }

        // 4. create the booking
        String bookingId = "B" + bookingCounter++;
        Booking booking = new Booking(bookingId, user, event, category, null, null);

        // 5. seat available -> confirm, otherwise waitlist
        if (category.getAvailableSeats() > 0) {
            category.setAvailableSeats(category.getAvailableSeats() - 1);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setTicketCode(makeTicketCode(booking));
        } else if (waitlistEnabled) {
            waitingListService.addToWaitlist(waitKey(event, category), booking);
        } else {
            throw new SeatNotAvailableException(
                    "No seats left in category " + category.getName());
        }

        bookings.put(bookingId, booking);
        return booking;
    }

    // ======================= CANCEL =======================
    public synchronized void cancelBooking(String bookingId) throws InvalidInputException {

        if (isEmpty(bookingId)) {
            throw new InvalidInputException("Booking id cannot be empty");
        }
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new InvalidInputException("Booking not found: " + bookingId);
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidInputException("Booking already cancelled: " + bookingId);
        }

        String key = waitKey(booking.getEvent(), booking.getCategory());

        // person was only waiting -> just leave the queue
        if (booking.getStatus() == BookingStatus.WAITLISTED) {
            waitingListService.removeFromWaitlist(key, booking);
            booking.setStatus(BookingStatus.CANCELLED);
            return;
        }

        // confirmed booking -> cancel it and free the seat
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setTicketCode(null);

        Booking next = waitingListService.promoteNextUser(key);
        if (next != null) {
            // the freed seat goes straight to the first waiting person
            next.setStatus(BookingStatus.CONFIRMED);
            next.setTicketCode(makeTicketCode(next));
        } else {
            TicketCategory category = booking.getCategory();
            category.setAvailableSeats(category.getAvailableSeats() + 1);
        }
    }

    // ======================= VIEW (for the menu) =======================
    public synchronized Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }

    public synchronized List<Booking> getBookingsByUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings.values()) {
            if (b.getUser().getId().equals(userId)) {
                result.add(b);
            }
        }
        return result;
    }

    // ======================= DEMO ONLY (Day 4) =======================
    // Deliberately NOT synchronized, to show the race condition.
    public boolean tryBookSeatUnsafe(String eventId, String categoryId) throws Exception {
        Event event = eventService.getEventById(eventId);
        TicketCategory category = findCategory(event, categoryId);
        if (category.getAvailableSeats() > 0) {                                // check
            Thread.sleep(5);                                                   // widen the race window
            category.setAvailableSeats(category.getAvailableSeats() - 1);      // act
            return true;
        }
        return false;
    }

    // ======================= HELPERS =======================
    private TicketCategory findCategory(Event event, String categoryId) {
        for (TicketCategory c : event.getTicketCategories()) {
            if (c.getId().equals(categoryId)) {
                return c;
            }
        }
        return null;
    }

    // one waiting queue per event + category
    private String waitKey(Event event, TicketCategory category) {
        return event.getId() + ":" + category.getId();
    }

    private String makeTicketCode(Booking booking) {
        return "TKT-" + booking.getEvent().getId() + "-" + booking.getBookingId();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}