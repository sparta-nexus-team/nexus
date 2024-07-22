package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.employee.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.token}")
    private String adminToken;

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (employeeRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        UserRole role = UserRole.USER;

        if (request.isAdmin()) {
            if (!adminToken.equals(request.getAdminToken())) {
                throw new IllegalArgumentException("어드민 토큰이 일치하지 않습니다.");
            }
            role = UserRole.MANAGER;
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Employee employee = new Employee(request,encodedPassword, role);
        employeeRepository.save(employee);
        return new SignupResponse(employee);
    }

    @Override
    public Long logout(Employee employee, HttpServletResponse response) {
        Long userId = employee.getId();
        employee.updateRefreshToken(null);
        employeeRepository.save(employee);

        return userId;
    }
}
