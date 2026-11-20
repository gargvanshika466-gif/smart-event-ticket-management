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
        // TODO
    }
    public void addCategoryToEvent(String eventId, TicketCategory category)
            throws EventNotFoundException, InvalidInputException {
        // TODO
    }

    public List<Event> getAllEvents() {
        // TODO
        return new ArrayList<>();
    }
    public void deleteEvent(String eventId) throws EventNotFoundException {
        // TODO
    }
}
