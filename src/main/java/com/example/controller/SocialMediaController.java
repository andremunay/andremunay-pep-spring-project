package com.example.controller;

import java.util.List;

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

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Handler for account registration
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if (account.getUsername() == null 
                || account.getUsername().isEmpty() 
                || account.getPassword() == null 
                || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        if (accountService.findByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        Account createdAccount = accountService.addAccount(account);
        return ResponseEntity.ok(createdAccount); // 200 Ok
    }

    // Handler for user login
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authenticatedAccount = accountService.authenticate(account.getUsername(), account.getPassword());
        if (authenticatedAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
        return ResponseEntity.ok(authenticatedAccount); // 200 Ok
    }

    // Handler for posting a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255 || !accountService.doesAccountExistById(message.getMessageId())) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        Message createdMessage = messageService.addMessage(message);
        return ResponseEntity.ok(createdMessage); // 200 Ok
    }

    // Handler for retrieving all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); // 200 Ok
    }

    // Handler for retrieving a message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); // 200 Ok
        }
        return ResponseEntity.ok(message); // 200 Ok
    }

    // Handler for deleting a message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        Integer rowsAffected = messageService.deleteMessageById(messageId);
        if (rowsAffected == 0) {
            return ResponseEntity.ok().build(); // 200 Ok
        }
        return ResponseEntity.ok(rowsAffected); // 200 Ok
    }

    // Handler for updating a message by ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message message) {
        message.setMessageId(messageId);
        Message existingMessage = messageService.getMessageById(messageId);

        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255 || existingMessage == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        Integer rowsAffected = messageService.updateMessageById(message);
        return ResponseEntity.ok(rowsAffected); // 200 Ok
    }

    // Handler for retrieving all messages by account ID
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        List<Message> messages = messageService.getAllMessages(accountId);
        return ResponseEntity.ok(messages); // 200 Ok
    }
}
