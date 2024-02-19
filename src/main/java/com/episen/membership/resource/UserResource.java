package com.episen.membership.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.episen.membership.model.User;
import com.episen.membership.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "users", produces = {"application/json"})
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService service;

    @GetMapping()
    public ResponseEntity<List<User>> getAll() {
        logger.info("Getting all users");
        List<User> users = service.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> add(@RequestBody User user) {
        logger.info("Adding user from request: {}", user);
        
        Map<String, Object> response = new HashMap<>();

        try {
            service.add(user);
            logger.info("User added successfully: {}", user.getUsername());

            response.put("success", true);
            response.put("message", "User added successfully");
            response.put("user", user);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error adding user: {}", e.getMessage());

            response.put("success", false);
            response.put("message", "Error adding user: " + e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{username}")
    public ResponseEntity<Object> getByUsername(@PathVariable String username) {
        logger.info("Getting user by username: {}", username);
        User user = service.getByUsername(username);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            response.put("username", username);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{username}")
    public ResponseEntity<Void> update(@PathVariable String username, @RequestBody User user) {
        logger.info("Updating user from request: {}", user);
        Map<String, String> response = new HashMap<>();
        try {
            service.update(username, user);
            logger.info("User updated successfully: {}", user.getUsername());
            response.put("message", "User updated successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error updating user: {}", e.getMessage());
            response.put("message", "User updating failed ");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String username) {
    logger.info("Deleting user by username: {}", username);
    service.delete(username);
    logger.info("User deleted successfully: {}", username);

    // message reponse apres suppression
    Map<String, String> response = new HashMap<>();
    response.put("message", "User deleted successfully");
    response.put("username", username);

    return new ResponseEntity<>(response, HttpStatus.OK);
}

}
