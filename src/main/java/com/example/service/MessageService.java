package com.example.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        LOGGER.info("Adding new message: {}", message.getMessageText());
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        LOGGER.info("Retrieving all messages");
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        LOGGER.info("Fetching message by ID: {}", messageId);
        return messageRepository.findById(messageId).orElse(null);
    }

    public Integer deleteMessageById(int messageId) {
        LOGGER.info("Attempting to delete message ID: {}", messageId);
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            messageRepository.deleteById(messageId);
            LOGGER.info("Message ID: {} deleted successfully", messageId);
            return 1;
        } else {
            LOGGER.warn("Message ID: {} not found", messageId);
            return 0;
        }
    }

    public Integer updateMessageById(Message message) {
        LOGGER.info("Updating message ID: {}", message.getMessageId());
        Optional<Message> existingMessage = messageRepository.findById(message.getMessageId());
        if (existingMessage.isPresent()) {
            messageRepository.save(message);
            LOGGER.info("Message ID: {} updated successfully", message.getMessageId());
            return 1;
        } else {
            LOGGER.warn("Message ID: {} not found", message.getMessageId());
            return 0;
        }
    }

    public List<Message> getAllMessages(int accountId) {
        LOGGER.info("Retrieving messages for account ID: {}", accountId);
        return messageRepository.findAllByAccountId(accountId);
    }
}
