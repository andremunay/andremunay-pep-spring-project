package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT a FROM Account a WHERE a.username = :username AND a.password = :password")
    Account findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
