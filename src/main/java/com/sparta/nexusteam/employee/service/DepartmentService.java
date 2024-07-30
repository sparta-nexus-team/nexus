package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Employee;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments(Employee employee);
    Department createDepartment(Department department, Employee employee);
    Department updateDepartment(Long id, Department departmentDetails, Employee employee);
    Long deleteDepartment(Long id);
}
