package model;

import enums.EventStatus;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id;
    private String title;
    private Venue venue;
    private String date;
    private EventStatus status;
    private List<TicketCategory> ticketCategories;

    public Event() {
        this.ticketCategories = new ArrayList<>();
    }

    public Event(String id, String title, Venue venue, String date, EventStatus status, List<TicketCategory> ticketCategories) {
        this.id = id;
        this.title = title;
        this.venue = venue;
        this.date = date;
        this.status = status;
        this.ticketCategories = (ticketCategories != null) ? ticketCategories : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }

    public void addTicketCategory(TicketCategory category) {
        if (this.ticketCategories == null) {
            this.ticketCategories = new ArrayList<>();
        }
        this.ticketCategories.add(category);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", venue=" + venue +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", ticketCategories=" + ticketCategories +
                '}';
    }
}