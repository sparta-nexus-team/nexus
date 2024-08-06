package com.sparta.nexusteam.employee.repository;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameAndCompany(String name, Company company);

    List<Department> findAllByCompany(Company company);

    Department findByNameAndCompany(String department, Company company);
}
