package model;

import enums.Role;

public class Attendee extends User {

    public Attendee() {
        super();
        setRole(Role.ATTENDEE);
    }

    public Attendee(String id, String name, String email) {
        super(id, name, email, Role.ATTENDEE);
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role=" + getRole() +
                '}';
    }
}