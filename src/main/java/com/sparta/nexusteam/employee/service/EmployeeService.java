package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.EmployeeRequest;
import com.sparta.nexusteam.employee.dto.EmployeeResponse;
import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface EmployeeService {
    SignupResponse signup(SignupRequest request);
    Long logout(Employee employee, HttpServletResponse response);
    String inviteEmployee(String email);
    SignupResponse setNewEmployee(String token, SignupRequest signupRequest);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    Long deleteEmployee(Long id);
    List<EmployeeResponse> getEmployeesByDepartment(String departmentName);
    List<EmployeeResponse> getEmployeesByUserName(String userName);
}
