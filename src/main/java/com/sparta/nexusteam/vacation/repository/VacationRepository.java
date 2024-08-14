package com.sparta.nexusteam.vacation.repository;

import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.Vacation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long>, VacationRepositoryCustom {
    List<Vacation> findByEndDateBeforeAndEmployeeIdAndApprovalStatusOrderByStartDateDesc(LocalDateTime currentTime, Long employeeId, ApprovalStatus approvalStatus);
    List<Vacation> findByEndDateAfterAndEmployeeIdAndApprovalStatusOrderByStartDateAsc(LocalDateTime currentTime, Long employeeId, ApprovalStatus approvalStatus);
}
