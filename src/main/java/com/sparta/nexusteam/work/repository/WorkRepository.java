package com.sparta.nexusteam.work.repository;

import com.sparta.nexusteam.employee.entity.Employee;


import com.sparta.nexusteam.work.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work,Long> {
    Optional<Work> findByEmployeeAndDate(Employee employee, LocalDate date);

    List<Work> findAllByDate(LocalDate date);

    List<Work> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
