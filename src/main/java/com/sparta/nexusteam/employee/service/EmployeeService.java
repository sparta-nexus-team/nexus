package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.servlet.http.HttpServletResponse;


public interface EmployeeService {
    SignupResponse signup(SignupRequest request);
    Long logout(Employee employee, HttpServletResponse response);
    String inviteEmployee(String email);
    SignupResponse setNewEmployee(String token, SignupRequest signupRequest);
}
