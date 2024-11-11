package com.example.demo.repository;

import com.example.demo.Enum.Role;
import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountById(long id);
    Account findAccountByUserName(String userName);
    Account findAccountByCode(String code);
    Account findAccountByEmail(String email);
    Account findAccountByRole(Role role);

    Page<Account> findAll(Pageable pageable);
    @Query("SELECT COUNT(u) FROM Account u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);
}