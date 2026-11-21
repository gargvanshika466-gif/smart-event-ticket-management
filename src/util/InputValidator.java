package util;

import exception.InvalidInputException;

public class InputValidator {
    public static void validateEmail(String email) throws InvalidInputException {
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        if (at <= 0) {
            throw new InvalidInputException("Email must have text before '@'");
        }
        if (dot < at + 2) {
            throw new InvalidInputException("Email must have a domain with '.' after '@'");
        }
        if (dot == email.length() - 1) {
            throw new InvalidInputException("Email cannot end with '.'");
        }
    }

    public static void validateTicketPrice(double price) throws InvalidInputException {
        if (price <= 0) {
            throw new InvalidInputException("Ticket price must be greater than 0");
        }
    }

    public static void validateSeatCount(int seats) throws InvalidInputException {
        if(seats <= 0) {
            throw new InvalidInputException("Seat count must be greater than 0");
        }
    }
}
