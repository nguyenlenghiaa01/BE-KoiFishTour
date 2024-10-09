package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountById(long id);
    Account findAccountByUserName(String userName);
    Account findAccountByCode(String code);
    Account findAccountByEmail(String email);

}