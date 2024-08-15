package com.sparta.nexusteam.work.repository;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkResponse;
import com.sparta.nexusteam.work.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work,Long> {
    Optional<Work> findByEmployeeAndWorkDate(Employee employee,Date date);
    Boolean existsByEmployeeAndWorkDate(Employee employee, LocalDate date);
    @Query("select w from Work w WHERE w.employee.company.id = :employee_id and w.workDate = :today")
    Page<Work> findWorkByToday(
            Pageable pageable,
            @Param("employee_id") Long employeeId,
            @Param("today") Date today
    );
    @Query("SELECT w FROM Work w WHERE w.employee.id = :employee_id AND w.workDate BETWEEN :startDate AND :endDate ORDER BY w.workDate DESC")
    Page<Work> findWorkByDateRange(
            Pageable pageable,
            @Param("employee_id") Long employeeId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    @Query("select w from Work w WHERE w.employee.company.id = :company_id and w.workDate = :today")
    Page<Work> findWorkByCompanyAndToday(
            Pageable pageable,
            @Param("company_id") Long companyId,
            @Param("today") Date today
    );

    @Query("select w from Work w where w.employee.company.id = :company_id and w.workDate between :startDate and :endDate")
    Page<Work> findWorkByCompanyAndDateRange(
            Pageable pageable,
            @Param("company_id") Long companyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
