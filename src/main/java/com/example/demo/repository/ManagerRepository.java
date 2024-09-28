package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Consulting;
import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Manager findManagerByUserName(String userName);
    Manager findManagerByCode(String code);
    List<Manager> findManagersByIsDeletedFalse();
    Manager findAccountById(long id);
}
