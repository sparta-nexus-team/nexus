package com.sparta.nexusteam.employee.repository;

import com.sparta.nexusteam.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

    Employee findByEmail(String email);
    Employee findByUserName(String username);
}
