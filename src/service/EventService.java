package service;

import exception.EventNotFoundException;
import exception.InvalidInputException;
import model.Event;
import model.TicketCategory;
import util.InputValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventService {
    private final HashMap<String, Event> events = new HashMap<>();

    public void createEvent(Event event) throws InvalidInputException {
        if (event == null) {
            throw new InvalidInputException("Event cannot be null");
        }
        if (event.getId() == null || event.getId().trim().isEmpty()) {
            throw new InvalidInputException("Event ID cannot be empty");
        }
        if (events.containsKey(event.getId())) {
            throw new InvalidInputException("Event ID already exists: " + event.getId());
        }
        events.put(event.getId(), event);
    }
    public void addCategoryToEvent(String eventId, TicketCategory category)
            throws EventNotFoundException, InvalidInputException {
        if (category == null) {
            throw new InvalidInputException("Ticket category cannot be null");
        }
        InputValidator.validateTicketPrice(category.getPrice());
        InputValidator.validateSeatCount(category.getTotalSeats());
        Event event = getEventById(eventId);
        event.addTicketCategory(category);
    }
    public Event getEventById(String eventId) throws EventNotFoundException {
        Event event = events.get(eventId);
        if (event == null) {
            throw new EventNotFoundException("Event not found: " + eventId);
        }
        return event;
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events.values());
    }
    public void deleteEvent(String eventId) throws EventNotFoundException {
        if (!events.containsKey(eventId)) {
            throw new EventNotFoundException("Event not found: " + eventId);
        }
        events.remove(eventId);
    }
}
