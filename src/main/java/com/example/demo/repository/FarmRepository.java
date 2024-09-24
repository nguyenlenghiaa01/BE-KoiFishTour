package com.example.demo.repository;

import com.example.demo.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    Farm findFarmById(long id);
    List<Farm> findFarmsByIsDeletedFalse();
}
