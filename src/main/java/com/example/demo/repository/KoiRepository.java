package com.example.demo.repository;

import com.example.demo.entity.KoiFish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoiRepository extends JpaRepository<KoiFish,Long> {
    // tim mot thang student bang id cua no
    // findStudentById(long id)
   KoiFish findKoiById(long id);
   // lay danh sach nhung thang student ma cai bien isDeleted = flase
    List<KoiFish>findKoiByIsDeletedFalse();

    Page<KoiFish> findAll(Pageable pageable);
    Page<KoiFish> findByIsDeletedFalse(Pageable pageable);
    List<KoiFish> findKoiFishesByFarmId(long id);
}
