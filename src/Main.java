
import java.util.Scanner;
import java.util.ArrayList;

import enums.EventStatus;
import exception.EventNotFoundException;
import exception.InvalidInputException;

import model.Event;
import model.Venue;
import model.TicketCategory;
import model.Attendee;

import service.EventService;
import service.UserService;
import enums.BookingStatus;

import exception.DuplicateBookingException;
import exception.SeatNotAvailableException;

import model.Booking;

import service.BookingService;
import service.WaitingListService;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final EventService eventService = new EventService();
    private static final UserService userService = new UserService();
    private static final WaitingListService waitingListService =
            new WaitingListService();

    private static final BookingService bookingService =
            new BookingService(eventService, userService, waitingListService);

    public static void main(String[] args) {

        System.out.println("====================================================");
        System.out.println("   EVENT TICKETING & CONCURRENT BOOKING SYSTEM");
        System.out.println("====================================================");

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice = getIntInput("Choose an option (1-4): ");

            switch (choice) {

                case 1:
                    handleAdminMenu();
                    break;

                case 2:
                    handleAttendeeMenu();
                    break;

                case 3:
                    runConcurrentDemo();
                    break;

                case 4:
                    System.out.println("\nSystem is shutting down. Thank you!");
                    running = false;
                    break;

                default:
                    System.out.println("[Error] Invalid option! Please enter a number between 1 and 4.");
            }
        }

        scanner.close();
    }

    // MAIN MENU
    private static void printMainMenu() {

        System.out.println("\n------------------- MAIN MENU -------------------");
        System.out.println("1. Admin / Organizer Menu");
        System.out.println("2. Attendee Menu");
        System.out.println("3. Run Concurrent Booking Demo");
        System.out.println("4. Exit");
        System.out.println("-------------------------------------------------");
    }

    // ADMIN MENU
    private static void handleAdminMenu() {

        boolean back = false;

        while (!back) {

            System.out.println("\n--- ADMIN / ORGANIZER MENU ---");
            System.out.println("1. Create New Event");
            System.out.println("2. Add Ticket Category to Event");
            System.out.println("3. View All Events");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Choose an option: ");

            switch (choice) {

                case 1:
                    try {

                        System.out.print("Enter Event ID: ");
                        String eventId = scanner.nextLine();

                        System.out.print("Enter Event Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Venue Name: ");
                        String venueName = scanner.nextLine();

                        System.out.print("Enter Venue City: ");
                        String city = scanner.nextLine();

                        int venueCapacity = getIntInput("Enter Venue Capacity: ");

                        System.out.print("Enter Event Date (DD-MM-YYYY): ");
                        String date = scanner.nextLine();

                        Venue venue = new Venue(
                                venueName,
                                city,
                                venueCapacity
                        );

                        Event event = new Event(
                                eventId,
                                name,
                                venue,
                                date,
                                EventStatus.ACTIVE,
                                new ArrayList<TicketCategory>()
                        );

                        eventService.createEvent(event);

                        System.out.println("[Success] Event created successfully!");

                    } catch (InvalidInputException e) {

                        System.out.println("[Error] " + e.getMessage());
                    }

                    break;

                case 2:
                    try {

                        System.out.print("Enter Event ID: ");
                        String targetEventId = scanner.nextLine();

                        System.out.print("Enter Category Name: ");
                        String categoryName = scanner.nextLine();

                        double price = getDoubleInput("Enter Ticket Price (INR): ");

                        int capacity = getIntInput("Enter Total Seats: ");

                        String categoryId = targetEventId + "-" + categoryName;

                        TicketCategory category = new TicketCategory(
                                categoryId,
                                categoryName,
                                price,
                                capacity,
                                capacity
                        );

                        eventService.addCategoryToEvent(
                                targetEventId,
                                category
                        );

                        System.out.println("[Success] Ticket category added successfully!");

                    } catch (EventNotFoundException | InvalidInputException e) {

                        System.out.println("[Error] " + e.getMessage());
                    }

                    break;

                case 3:
                    System.out.println("\n--- ALL EVENTS ---");

                    if (eventService.getAllEvents().isEmpty()) {

                        System.out.println("No events are currently available.");

                    } else {

                        for (Event event : eventService.getAllEvents()) {
                            System.out.println(event);
                        }
                    }

                    break;

                case 4:
                    back = true;
                    break;

                default:
                    System.out.println("[Warning] Please choose a valid option.");
            }
        }
    }

    // ATTENDEE MENU
    private static void handleAttendeeMenu() {

        boolean back = false;

        while (!back) {

            System.out.println("1. Register New Attendee");
            System.out.println("2. View Available Events");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Booking");
            System.out.println("TEST MENU UPDATED");
            System.out.println("5. View My Bookings");
            System.out.println("6. Back to Main Menu");

            int choice = getIntInput("Choose an option: ");

            switch (choice) {

                case 1:
                    try {

                        System.out.print("Enter User ID: ");
                        String userId = scanner.nextLine();

                        System.out.print("Enter Your Name: ");
                        String userName = scanner.nextLine();

                        System.out.print("Enter Email ID: ");
                        String email = scanner.nextLine();

                        Attendee attendee = new Attendee(
                                userId,
                                userName,
                                email
                        );

                        userService.registerUser(attendee);

                        System.out.println("[Success] Attendee registered successfully!");

                    } catch (InvalidInputException e) {

                        System.out.println("[Error] " + e.getMessage());
                    }

                    break;

                case 2:
                    System.out.println("\n--- AVAILABLE EVENTS ---");

                    if (eventService.getAllEvents().isEmpty()) {

                        System.out.println("No events are currently available.");

                    } else {

                        for (Event event : eventService.getAllEvents()) {
                            System.out.println(event);
                        }
                    }

                    break;

                case 3:
                    bookTicket();
                    break;

                case 4:
                    cancelBooking();
                    break;

                case 5:
                    viewMyBookings();
                    break;

                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("[Warning] Please choose a valid option.");
            }
        }
    }
    private static void bookTicket() {

        System.out.println("\n--- BOOK TICKET ---");

        try {
            System.out.print("Enter Attendee ID: ");
            String userId = scanner.nextLine();

            System.out.print("Enter Event ID: ");
            String eventId = scanner.nextLine();

            Event event = eventService.getEventById(eventId);

            System.out.println("\nAvailable Ticket Categories:");

            for (TicketCategory category : event.getTicketCategories()) {
                System.out.println(
                        category.getId() + " - " +
                                category.getName() +
                                " | Price: " + category.getPrice() +
                                " | Available Seats: " + category.getAvailableSeats()
                );
            }

            System.out.print("\nEnter Category ID: ");
            String categoryId = scanner.nextLine();

            Booking booking = bookingService.bookTicket(
                    userId, eventId, categoryId
            );

            System.out.println("\nBooking Successful!");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Status: " + booking.getStatus());

            if (booking.getTicketCode() != null) {
                System.out.println("Ticket Code: " + booking.getTicketCode());
            }

        } catch (InvalidInputException | EventNotFoundException |
                 DuplicateBookingException | SeatNotAvailableException e) {

            System.out.println("\n[Error] " + e.getMessage());
        }
    }
    private static void cancelBooking() {

        System.out.println("\n--- CANCEL BOOKING ---");

        try {
            System.out.print("Enter Booking ID: ");
            String bookingId = scanner.nextLine();

            bookingService.cancelBooking(bookingId);

            System.out.println("\nBooking cancelled successfully!");

        } catch (InvalidInputException e) {

            System.out.println("\n[Error] " + e.getMessage());
        }
    }
    private static void viewMyBookings() {

        System.out.println("\n--- MY BOOKINGS ---");

        try {
            System.out.print("Enter Attendee ID: ");
            String userId = scanner.nextLine();

            userService.getUserById(userId);

            ArrayList<Booking> myBookings =
                    new ArrayList<>(bookingService.getBookingsByUser(userId));

            if (myBookings.isEmpty()) {
                System.out.println("No bookings found.");
            } else {
                for (Booking booking : myBookings) {
                    System.out.println(booking);
                }
            }

        } catch (InvalidInputException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    // CONCURRENT BOOKING DEMO
    private static void runConcurrentDemo() {

        System.out.println("[Day 3] ConcurrentBookingDemo will be connected later.");
    }

    // INTEGER INPUT
    private static int getIntInput(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("[Input Error] Please enter a valid number.");
            }
        }
    }

    // DOUBLE INPUT
    private static double getDoubleInput(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Double.parseDouble(scanner.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("[Input Error] Please enter a valid amount.");
            }
        }
    }
}
