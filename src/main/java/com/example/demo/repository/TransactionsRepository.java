package com.example.demo.repository;

import com.example.demo.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions,Long> {
    Transactions findPTransactionsById(long id);
}
