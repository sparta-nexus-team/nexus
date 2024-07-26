package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.*;
import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface EmployeeService {
    SignupResponse signup(SignupRequest request);
    Long logout(Employee employee, HttpServletResponse response);
    String inviteEmployee(String email, Employee employee);
    SignupResponse setNewEmployee(String token, InviteSignupRequest invitesignupRequest);
    List<EmployeeResponse> getAllEmployees(Employee employeeDetail);
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    Long deleteEmployee(Long id);
    List<EmployeeResponse> getEmployeesByDepartment(String departmentName, Employee employee);
    List<EmployeeResponse> getEmployeesByUserName(String userName, Employee employeeDetail);
}
