package service;
import exception.InvalidInputException;
import model.User;
import util.InputValidator;
import java.util.HashMap;
public class UserService {
    private final HashMap<String, User> users = new HashMap<>();

    public void registerUser(User user) throws InvalidInputException {
        if (user == null) {
            throw new InvalidInputException("User cannot be null");
        }

        if (user.getId() == null || user.getId().trim().isEmpty()) {
            throw new InvalidInputException("User ID cannot be empty");
        }
        InputValidator.validateEmail(user.getEmail());

        if (users.containsKey(user.getId())) {
            throw new InvalidInputException("User ID already exists: " + user.getId());
        }
        users.put(user.getId(), user);
    }

    public User getUserById(String userId) throws InvalidInputException {
        User user = users.get(userId);
        if (user == null) {
            throw new InvalidInputException("User not found: " + userId);
        }
        return user;
    }
}
