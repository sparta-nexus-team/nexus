package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.employee.dto.SignupResponse;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Invitation;
import com.sparta.nexusteam.employee.entity.Position;
import com.sparta.nexusteam.employee.entity.UserRole;
import com.sparta.nexusteam.employee.repository.EmployeeRepository;
import com.sparta.nexusteam.employee.repository.InvitationRepository;
import com.sparta.nexusteam.employee.util.PasswordGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final InvitationRepository invitationRepository;

    private final JavaMailSender mailSender;

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
        Employee employee = new Employee(request,encodedPassword, Position.CHAIRMAN ,role);
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

    @Override
    public String inviteEmployee(String email) {
        String token = UUID.randomUUID().toString();
        String initialUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String initialPassword = PasswordGenerator.generatePassword();
        Invitation invitation = new Invitation(email, token, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), initialUsername, passwordEncoder.encode(initialPassword));
        invitationRepository.save(invitation);

        return sendInvitationEmail(email, token, initialUsername, initialPassword);
    }

    @Override
    public SignupResponse setNewEmployee(String token, SignupRequest signupRequest) {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new RuntimeException ("Invalid token");
        }

        UserRole role = UserRole.USER;
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        Employee employee = new Employee(signupRequest,encodedPassword, Position.WORKER ,role);
        employeeRepository.save(employee);
        return new SignupResponse(employee);
    }

    private String sendInvitationEmail(String email, String token, String initialUsername, String initialPassword) {
        String subject = "Employee Invitation";
        String confirmationUrl = "/registerEmployee?token=" + token;
        String message = "Click the link to register: " + confirmationUrl + "\n" +
                "Your username: " + initialUsername + "\n" +
                "Your password: " + initialPassword;

        try {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(email);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            mailSender.send(emailMessage);
            return "Invitation email sent successfully.";
        } catch (Exception e) {
            return "Failed to send invitation email.";
        }
    }
}
