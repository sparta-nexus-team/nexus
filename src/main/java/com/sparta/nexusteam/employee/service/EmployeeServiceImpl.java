package com.sparta.nexusteam.employee.service;

import com.sparta.nexusteam.employee.dto.*;
import com.sparta.nexusteam.employee.entity.*;
import com.sparta.nexusteam.employee.repository.CompanyRepository;
import com.sparta.nexusteam.employee.repository.EmployeeRepository;
import com.sparta.nexusteam.employee.repository.InvitationRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final InvitationRepository invitationRepository;

    private final CompanyRepository companyRepository;

    private final JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (employeeRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if(companyRepository.existsByName(request.getCompanyName())) {
            throw new IllegalArgumentException("이미 존재하는 회사입니다.");
        }

        Company company = new Company(request.getCompanyName());

        companyRepository.save(company);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Employee employee = new Employee(request,encodedPassword, Position.CHAIRMAN ,UserRole.MANAGER, company);

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
    public String inviteEmployee(String email, Employee employee) {
        String token = UUID.randomUUID().toString();
        Invitation invitation = new Invitation(email, token, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), employee.getCompany().getName());

        invitationRepository.save(invitation);

        return sendInvitationEmail(email, token);
    }

    @Override
    public SignupResponse setNewEmployee(String token, InviteSignupRequest invitesignupRequest) {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new RuntimeException ("유효하지 않은 token");
        }

        Company company = companyRepository.findByName(invitation.getCompanyName());
        String encodedPassword = passwordEncoder.encode(invitesignupRequest.getPassword());
        Employee employee = new Employee(invitesignupRequest,encodedPassword, Position.WORKER ,UserRole.USER, company);

        employeeRepository.save(employee);

        return new SignupResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees(Employee employeeDetail) {
        List<Employee> employees = employeeRepository.findAllByCompany(employeeDetail.getCompany());
        List<EmployeeResponse> responseList = new ArrayList<>();

        for(Employee employee: employees){
            EmployeeResponse employeeResponse = new EmployeeResponse(employee);
            responseList.add(employeeResponse);
        }

        return responseList;
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String departmentName, Employee employeeDetail) {
        List<Employee> employees = employeeRepository.findByDepartmentNameAndCompany(departmentName, employeeDetail.getCompany());
        List<EmployeeResponse> responseList = new ArrayList<>();

        for(Employee employee: employees){
            EmployeeResponse employeeResponse = new EmployeeResponse(employee);
            responseList.add(employeeResponse);
        }

        return responseList;
    }

    @Override
    public List<EmployeeResponse> getEmployeesByUserName(String userName, Employee employeeDetail) {
        List<Employee> employees = employeeRepository.findByUserNameAndCompany(userName, employeeDetail.getCompany());
        List<EmployeeResponse> responseList = new ArrayList<>();

        for(Employee employee: employees){
            EmployeeResponse employeeResponse = new EmployeeResponse(employee);
            responseList.add(employeeResponse);
        }

        return responseList;
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 근로자 입니다." + id));

        return new EmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 근로자 입니다." + id));

        employee.updateProfile(request);

        employeeRepository.save(employee);

        return new EmployeeResponse(employee);
    }

    @Override
    public Long deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 근로자 입니다." + id));

        employeeRepository.delete(employee);

        return employee.getId();
    }

    private String sendInvitationEmail(String email, String token) {
        String subject = "Employee Invitation";
        String confirmationUrl = "/employee/registerEmployee?token=" + token;
        String message = "Click the link to register: " + confirmationUrl + "\n";

        try {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(email);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            mailSender.send(emailMessage);
            return "Invitation email sent successfully.";
        } catch (Exception e) {
            throw new RuntimeException("전송 실패");
        }
    }
}
