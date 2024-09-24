package com.example.demo.repository;
import com.example.demo.entity.ConsultingStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingStaffRepository extends JpaRepository<ConsultingStaff,Long> {
    ConsultingStaff findConsultingStaffById(long id);
    List<ConsultingStaff> findConsultingStaffsByIsDeletedFalse();
}
