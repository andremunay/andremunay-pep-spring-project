package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
@RequestMapping
public class SocialMediaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialMediaController.class);
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Handler for account registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        LOGGER.info("Attempting to register account: {}", account.getUsername());
        if (account.getUsername() == null 
                || account.getUsername().isEmpty() 
                || account.getPassword() == null 
                || account.getPassword().length() < 4) {
            String message = "Invalid request: 'username' must not be null or empty, and 'password' must be at least 4 characters long.";
            return ResponseEntity.badRequest().body(message); // 400 Bad Request
        }
        if (accountService.findByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists."); // 409 Conflict
        }
        Account createdAccount = accountService.addAccount(account);
        return ResponseEntity.ok(createdAccount); // 200 Ok
    }

    // Handler for user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        LOGGER.info("Authenticating user: {}", account.getUsername());
        Account authenticatedAccount = accountService.authenticate(account.getUsername(), account.getPassword());
        if (authenticatedAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials."); // 401 Unauthorized
        }
        return ResponseEntity.ok(authenticatedAccount); // 200 Ok
    }

    // Handler for posting a new message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        LOGGER.info("Creating message for user ID: {}", message.getPostedBy());
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255 || !accountService.doesAccountExistById(message.getPostedBy())) {
            String errorMessage = "Invalid request: 'messageText' cannot be empty, 'messageText' cannot exceed 255 characters, and the account with the provided 'postedBy' ID must exist.";
            return ResponseEntity.badRequest().body(errorMessage); // 400 Bad Request
        }
        Message createdMessage = messageService.addMessage(message);
        return ResponseEntity.ok(createdMessage); // 200 Ok
    }

    // Handler for retrieving all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        LOGGER.info("Fetching all messages");
        return ResponseEntity.ok(messageService.getAllMessages()); // 200 Ok
    }

    // Handler for retrieving a message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId) {
        LOGGER.info("Fetching message by ID: {}", messageId);
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); // 200 Ok
        }
        return ResponseEntity.ok(message); // 200 Ok
    }

    // Handler for deleting a message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId) {
        LOGGER.info("Deleting message by ID: {}", messageId);
        Integer rowsAffected = messageService.deleteMessageById(messageId);
        if (rowsAffected == 0) {
            return ResponseEntity.ok().build(); // 200 Ok
        }
        return ResponseEntity.ok(rowsAffected); // 200 Ok
    }

    // Handler for updating a message by ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        LOGGER.info("Updating message ID: {}", messageId);
        message.setMessageId(messageId);
        Message existingMessage = messageService.getMessageById(messageId);

        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255 || existingMessage == null) {
            String errorMessage = "Invalid request: 'messageText' must not be empty, must not exceed 255 characters, and the specified message must exist.";
            return ResponseEntity.badRequest().body(errorMessage); // 400 Bad Request
        }
        Integer rowsAffected = messageService.updateMessageById(message);
        return ResponseEntity.ok(rowsAffected); // 200 Ok
    }

    // Handler for retrieving all messages by account ID
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        LOGGER.info("Fetching messages for account ID: {}", accountId);
        return ResponseEntity.ok(messageService.getAllMessages(accountId)); // 200 Ok
    }
}
