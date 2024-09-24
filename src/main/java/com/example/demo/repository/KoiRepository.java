package com.example.demo.repository;

import com.example.demo.entity.KoiFish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiRepository extends JpaRepository<KoiFish,String> {
    // tim mot thang student bang id cua no
    // findStudentById(long id)
   KoiFish findKoiById(String id);
   // lay danh sach nhung thang student ma cai bien isDeleted = flase
    List<KoiFish>findKoiByIsDeletedFalse();
}
