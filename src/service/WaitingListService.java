package service;

import enums.BookingStatus;
import model.Booking;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class WaitingListService {

    // key = event id, value = people waiting for that event (first in, first out)
    private Map<String, Queue<Booking>> waitingLists = new HashMap<>();

    // Put a booking at the END of the queue for this event
    public synchronized void addToWaitlist(String eventId, Booking booking) {
        if (!waitingLists.containsKey(eventId)) {
            waitingLists.put(eventId, new LinkedList<>());
        }
        booking.setStatus(BookingStatus.WAITLISTED);
        waitingLists.get(eventId).add(booking);
    }

    // Take the FIRST person out of the queue. Returns null if nobody is waiting.
    public synchronized Booking promoteNextUser(String eventId) {
        Queue<Booking> queue = waitingLists.get(eventId);
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    // Used when a waiting person cancels before getting a seat
    public synchronized void removeFromWaitlist(String eventId, Booking booking) {
        Queue<Booking> queue = waitingLists.get(eventId);
        if (queue != null) {
            queue.remove(booking);
        }
    }

    public synchronized int getWaitingCount(String eventId) {
        Queue<Booking> queue = waitingLists.get(eventId);
        return (queue == null) ? 0 : queue.size();
    }
}