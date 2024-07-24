package com.sparta.nexusteam.employee.repository;

import com.sparta.nexusteam.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

    List<Employee> findByDepartmentName(String departmentName);

    List<Employee> findByUserName(String username);
}
