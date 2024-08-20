package com.sparta.nexusteam.salary.repository;

import com.sparta.nexusteam.salary.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAllByEmployeeId(Long id);
}