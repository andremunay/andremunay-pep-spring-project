package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account) {
        LOGGER.info("Adding new account: {}", account.getUsername());
        return accountRepository.save(account);
    }

    public boolean findByUsername(String username) {
        LOGGER.info("Checking existence of username: {}", username);
        return accountRepository.existsByUsername(username);
    }

    public Account authenticate(String username, String password) {
        LOGGER.info("Checking existence of username: {}", username);
        Account account = accountRepository.findByUsernameAndPassword(username, password);
        if (account == null) {
            LOGGER.warn("Authentication failed for user: {}", username);
        }
        return account;
    }

    public boolean doesAccountExistById(Integer accountId) {
        if (accountId == null) {
            LOGGER.warn("Null account ID provided for existence check");
            return false;
        }
        LOGGER.info("Checking existence of account ID: {}", accountId);
        return accountRepository.existsById(accountId);
    }
}
