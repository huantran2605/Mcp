package com.mcp.tools;

import com.mcp.user.User;
import com.mcp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserTools {

    private final UserRepository repo;

    public UserTools(UserRepository repo) {
        this.repo = repo;
    }

    @Tool(name = "getUserByUsername",
            description = "Look up a user by username and return profile fields.")
    public Map<String, Object> getUserByUsername(
            @ToolParam(description = "The username to look up") String username) {
        log.info("Attempting to retrieve user by username: {}", username);
        return repo.findByUsername(username)
                .<Map<String, Object>>map(u -> {
                    log.info("User found: {}", username);
                    return Map.of(
                            "username", u.getUsername(),
                            "fullName", u.getFullName(),
                            "email", u.getEmail(),
                            "role", u.getRole()
                    );
                })
                .orElseGet(() -> {
                    log.warn("User not found: {}", username);
                    return Map.of("error", "USER_NOT_FOUND", "username", username);
                });
    }

    @Tool(name = "addUser",
            description = "Adds a new user to the system with a unique username, full name, email, and role. " +
                    "Returns the created user's details or an error if the username already exists or " +
                    "if any required fields are missing.")
    public Map<String, Object> addUser(
            @ToolParam(description = "The unique username for the new user (required)") String username,
            @ToolParam(description = "The full name of the new user (required)") String fullName,
            @ToolParam(description = "The email address of the new user (required)") String email,
            @ToolParam(description = "The role of the new user (e.g., 'user', 'admin') (required)") String role) {

        log.info("Attempting to add new user with username: {}, fullName: {}, email: {}, role: {}", username, fullName, email, role);

        Map<String, Object> errors = new HashMap<>();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username cannot be empty.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.put("fullName", "Full name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email cannot be empty.");
        }
        if (role == null || role.trim().isEmpty()) {
            errors.put("role", "Role cannot be empty.");
        }

        if (!errors.isEmpty()) {
            log.warn("Missing required fields for user creation: {}", errors);
            errors.put("error", "MISSING_REQUIRED_FIELDS");
            return errors;
        }

        Optional<User> existingUser = repo.findByUsername(username);
        if (existingUser.isPresent()) {
            log.warn("User with username {} already exists. Aborting user creation.", username);
            return Map.of("error", "USER_ALREADY_EXISTS", "username", username);
        }

        User newUser = User.builder()
                .username(username)
                .fullName(fullName)
                .email(email)
                .role(role)
                .build();

        User savedUser = repo.save(newUser);
        log.info("Successfully created new user: {}", savedUser.getUsername());

        return Map.of(
                "username", savedUser.getUsername(),
                "fullName", savedUser.getFullName(),
                "email", savedUser.getEmail(),
                "role", savedUser.getRole(),
                "status", "USER_CREATED"
        );
    }
}
