package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department createDepartment(Department department);
    Department updateDepartment(Long id, Department departmentDetails);
    Long deleteDepartment(Long id);
}
