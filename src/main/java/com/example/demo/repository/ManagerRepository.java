package com.example.demo.repository;

import com.example.demo.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager,String> {
    Manager findManagerById(String ManagerId);
    List<Manager> findManagersByIsDeletedFalse();
}
