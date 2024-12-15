package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public boolean findByUsername(boolean exists) {
        return accountRepository.findAll().stream()
            .anyMatch(account -> exists == (account.getUsername() != null && !account.getUsername().isEmpty()));
    }

    public Account authenticate(String username, String password) {
        Optional<Account> account = accountRepository.findAll().stream()
            .filter(acc -> username.equals(acc.getUsername()) && password.equals(acc.getPassword()))
            .findFirst();
        return account.orElse(null);
    }
}
