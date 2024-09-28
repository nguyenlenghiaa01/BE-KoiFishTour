package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Consulting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingRepository extends JpaRepository<Consulting,Long> {
    Consulting findConsultingByUserName(String userName);
    Consulting findConsultingByCode(String code);
    List<Consulting> findConsultingByIsDeletedFalse();
    Consulting findAccountById(long id);
}
