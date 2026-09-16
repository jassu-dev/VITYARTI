package com.vityarthi.academic.service;

import com.vityarthi.academic.exception.AuthenticationException;
import com.vityarthi.academic.exception.EntityNotFoundException;
import com.vityarthi.academic.exception.ValidationException;
import com.vityarthi.academic.factory.UserFactory;
import com.vityarthi.academic.model.Role;
import com.vityarthi.academic.model.User;
import com.vityarthi.academic.repository.FileUserRepository;
import com.vityarthi.academic.util.SecurityUtils;

import java.util.Optional;

/**
 * Service orchestrating authentication, user registrations, and session state.
 */
public class AuthenticationService {

    private final FileUserRepository userRepository;
    private User currentUser;

    public AuthenticationService(FileUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String userIdOrEmail, String rawPassword) throws AuthenticationException {
        if (userIdOrEmail == null || rawPassword == null || rawPassword.isEmpty()) {
            throw new AuthenticationException("Credentials cannot be blank.");
        }

        Optional<User> userOpt = userRepository.findById(userIdOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(userIdOrEmail);
        }

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("No user found with identifier: " + userIdOrEmail);
        }

        User user = userOpt.get();
        if (!user.isActive()) {
            throw new AuthenticationException("User account is inactive. Please contact administration.");
        }

        if (!SecurityUtils.verifyPassword(rawPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid password provided.");
        }

        this.currentUser = user;
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User registerUser(
            Role role,
            String userId,
            String name,
            String email,
            String rawPassword,
            String param1,
            String param2
    ) throws ValidationException {
        if (userRepository.existsById(userId)) {
            throw new ValidationException("User ID '" + userId + "' is already registered.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email '" + email + "' is already registered to another user.");
        }
        if (rawPassword == null || rawPassword.length() < 4) {
            throw new ValidationException("Password must contain at least 4 characters.");
        }

        User newUser = UserFactory.createUser(role, userId, name, email, rawPassword, param1, param2);
        return userRepository.save(newUser);
    }

    public User getUserById(String userId) throws EntityNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
    }
}
