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

    public boolean findByUsername(String username) {
        return accountRepository.findAll().stream()
            .anyMatch(account -> username.equals(account.getUsername()));
    }

    public Account authenticate(String username, String password) {
        Optional<Account> account = accountRepository.findAll().stream()
            .filter(acc -> username.equals(acc.getUsername()) && password.equals(acc.getPassword()))
            .findFirst();
        return account.orElse(null);
    }

    public boolean doesAccountExistById(Integer accountId) {
        if (accountId == null) {
            return false;
        }
        return accountRepository.existsById(accountId);
    }
}
