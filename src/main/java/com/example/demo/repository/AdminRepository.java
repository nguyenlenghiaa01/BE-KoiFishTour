package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findAccountById(long id);
    Admin findAccountByUserName(String userName);
    Admin findAccountByCode(String code);
    List<Admin> findAccountsByIsDeletedFalse();
}
