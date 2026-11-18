import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("   EVENT TICKETING & CONCURRENT BOOKING SYSTEM");
        System.out.println("====================================================");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getIntInput("Apna option chunein (1-4): ");

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
                    System.out.println("\nSystem band ho raha hai. Dhanyavaad!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[Error] Galat option! Kripya 1 se 4 ke beech number dalein.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n------------------- MAIN MENU -------------------");
        System.out.println("1. Admin / Organizer Menu");
        System.out.println("2. Attendee Menu");
        System.out.println("3. Run Concurrent Booking Demo (Multithreading)");
        System.out.println("4. Exit");
        System.out.println("-------------------------------------------------");
    }

    // ==================== 1. ADMIN / ORGANIZER MENU ====================
    private static void handleAdminMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ADMIN / ORGANIZER MENU ---");
            System.out.println("1. Create New Event");
            System.out.println("2. Add Ticket Category to Event");
            System.out.println("3. View All Events");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Option chunein: ");

            switch (choice) {
                case 1:
                    System.out.print("Event ID dalein: ");
                    String eventId = scanner.nextLine();
                    System.out.print("Event ka Naam: ");
                    String name = scanner.nextLine();
                    System.out.print("Venue (Jagah): ");
                    String venue = scanner.nextLine();
                    System.out.print("Date (DD-MM-YYYY): ");
                    String date = scanner.nextLine();
                    System.out.println("[Day 1 Ready] Event '" + name + "' ka input mil gaya!");
                    break;

                case 2:
                    System.out.print("Event ID dalein: ");
                    String targetEventId = scanner.nextLine();
                    System.out.print("Category Name (e.g., VIP, General): ");
                    String categoryName = scanner.nextLine();
                    double price = getDoubleInput("Ticket Price (INR): ");
                    int capacity = getIntInput("Total Seats (Capacity): ");
                    System.out.println("[Day 1 Ready] Category '" + categoryName + "' (" + capacity + " seats @ Rs." + price + ") ka input mil gaya!");
                    break;

                case 3:
                    System.out.println("[Day 1 Ready] Yahan Day 2 mein EventService se saare events dikhenge.");
                    break;

                case 4:
                    back = true;
                    break;

                default:
                    System.out.println("[Warning] Sahi option chunein (1-4).");
            }
        }
    }

    // ==================== 2. ATTENDEE MENU ====================
    private static void handleAttendeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ATTENDEE MENU ---");
            System.out.println("1. Register New Attendee");
            System.out.println("2. View Available Events");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Back to Main Menu");

            int choice = getIntInput("Option chunein: ");

            switch (choice) {
                case 1:
                    System.out.print("User ID dalein: ");
                    String userId = scanner.nextLine();
                    System.out.print("Apna Naam dalein: ");
                    String userName = scanner.nextLine();
                    System.out.print("Email ID: ");
                    String email = scanner.nextLine();
                    System.out.println("[Day 1 Ready] User '" + userName + "' ka input mil gaya!");
                    break;

                case 2:
                    System.out.println("[Day 1 Ready] Yahan Day 2 mein available events ki list dikhegi.");
                    break;

                case 3:
                    System.out.print("User ID dalein: ");
                    String bUserId = scanner.nextLine();
                    System.out.print("Event ID dalein: ");
                    String bEventId = scanner.nextLine();
                    System.out.print("Ticket Category: ");
                    String bCategory = scanner.nextLine();
                    int quantity = getIntInput("Kitni tickets chahiye?: ");
                    System.out.println("[Day 1 Ready] " + quantity + " tickets book karne ka input mil gaya!");
                    break;

                case 4:
                    System.out.print("Cancel karne ke liye Booking ID dalein: ");
                    String cancelId = scanner.nextLine();
                    System.out.println("[Day 1 Ready] Booking ID '" + cancelId + "' cancel karne ka input mil gaya!");
                    break;

                case 5:
                    back = true;
                    break;

                default:
                    System.out.println("[Warning] Sahi option chunein (1-5).");
            }
        }
    }

    // ==================== 3. CONCURRENT DEMO ====================
    private static void runConcurrentDemo() {
        System.out.println("\n[Day 1 Ready] Yahan Day 3 mein Member 3 ki ConcurrentBookingDemo class run hogi.");
    }

    // ==================== SAFE INPUT HELPERS ====================
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Kripya sirf valid number dalein.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Kripya sahi amount dalein.");
            }
        }
    }
}