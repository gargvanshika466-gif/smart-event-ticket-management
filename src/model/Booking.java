package model;

import enums.BookingStatus;

public class Booking {
    private String bookingId;
    private User user;
    private Event event;
    private TicketCategory category;
    private BookingStatus status;
    private String ticketCode;

    public Booking() {
    }

    public Booking(String bookingId, User user, Event event, TicketCategory category, BookingStatus status, String ticketCode) {
        this.bookingId = bookingId;
        this.user = user;
        this.event = event;
        this.category = category;
        this.status = status;
        this.ticketCode = ticketCode;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", user=" + user +
                ", event=" + event +
                ", category=" + category +
                ", status=" + status +
                ", ticketCode='" + ticketCode + '\'' +
                '}';
    }
}