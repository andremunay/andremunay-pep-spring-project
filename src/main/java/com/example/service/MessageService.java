package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public Message deleteMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            messageRepository.deleteById(messageId);
            return message.get();
        }
        return null;
    }

    public Message updateMessageById(Message message) {
        Optional<Message> existingMessage = messageRepository.findById(message.getMessageId());
        if (existingMessage.isPresent()) {
            return messageRepository.save(message);
        }
        return null;
    }

    public List<Message> getAllMessages(int accountId) {
        return messageRepository.findAll().stream()
            .filter(message -> message.getPostedBy() != null && message.getPostedBy().equals(accountId))
            .toList();
    }
}
