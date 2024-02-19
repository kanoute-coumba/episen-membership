package com.episen.membership.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.episen.membership.model.User;
import com.episen.membership.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    public void add(User user) {
        logger.info("Adding user: {}", user.getUsername());

        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getEmail())) {
            logger.error("Invalid user data: {}", user);
            throw new RuntimeException("Username and email are required");
        }

        User existingUser = repository.findByUsername(user.getUsername());

        if (existingUser != null) {
            logger.error("User already exists: {}", user.getUsername());
            throw new RuntimeException("User already exists");
        }

        repository.save(user);
        logger.info("User added successfully: {}", user.getUsername());
    }

    public List<User> getAll() {
        logger.info("Getting all users");
        return repository.findAll();
    }

    public User getByUsername(String username) {
        logger.info("Getting user by username: {}", username);
        return repository.findByUsername(username);
    }

    public void update(String username, User updatedUser) {
        logger.info("Updating user: {}", username);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(updatedUser.getEmail())) {
            logger.error("Invalid user data: {}", updatedUser);
            throw new RuntimeException("Username and email are required");
        }

        User existingUser = repository.findByUsername(username);

        if (existingUser == null) {
            logger.error("User not found: {}", username);
            throw new RuntimeException("User not found");
        }

        // Vérifier si le nouveau nom d'utilisateur est différent de l'ancien
        if (!username.equals(updatedUser.getUsername())) {
            // Vérifier l'unicité du nouveau nom d'utilisateur
            User userWithNewUsername = repository.findByUsername(updatedUser.getUsername());
            if (userWithNewUsername != null) {
                logger.error("New username already exists: {}", updatedUser.getUsername());
                throw new RuntimeException("New username already exists");
            }
        }

        // Mettez à jour les champs de l'utilisateur existant avec les nouvelles valeurs
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRoles(updatedUser.getRoles());

        // Mettre à jour le nom d'utilisateur s'il a changé
        if (!username.equals(updatedUser.getUsername())) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        // Utilisez save pour mettre à jour l'utilisateur existant
        repository.save(existingUser);

        logger.info("User updated successfully: {}", username);
    }

    // ...

    public void delete(String username) {
        logger.info("Deleting user by username: {}", username);
        repository.deleteByUsername(username);
        logger.info("User deleted successfully: {}", username);
    }
}
