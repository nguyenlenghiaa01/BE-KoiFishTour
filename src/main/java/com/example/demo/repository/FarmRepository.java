package com.example.demo.repository;

import com.example.demo.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, String> {
    Farm findFarmById(String FarmId);
    List<Farm> findFarmsByIsDeletedFalse();
}
