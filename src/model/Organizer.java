package model;

import enums.Role;

public class Organizer extends User {

    public Organizer() {
        super();
        setRole(Role.ORGANIZER);
    }

    public Organizer(String id, String name, String email) {
        super(id, name, email, Role.ORGANIZER);
    }

    @Override
    public String toString() {
        return "Organizer{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role=" + getRole() +
                '}';
    }
}