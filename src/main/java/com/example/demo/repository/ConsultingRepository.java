package com.example.demo.repository;

import com.example.demo.entity.Consulting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingRepository extends JpaRepository<Consulting,Long> {
    Consulting findConsultingById(long id);
    List<Consulting> findConsultingsByIsDeletedFalse();
}
