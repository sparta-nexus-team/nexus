package com.sparta.nexusteam.employee.entity;

import com.sparta.nexusteam.employee.dto.EmployeeRequest;
import com.sparta.nexusteam.employee.dto.InviteSignupRequest;
import com.sparta.nexusteam.employee.dto.SignupRequest;
import com.sparta.nexusteam.vacation.entity.Vacation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountId; //로그인 아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName; //이름

    @Email
    @Column(nullable = false)
    @Size(max = 255)
    private String email;

    @Pattern(regexp = "^(01[016789])-?(\\d{3,4})-?(\\d{4})$")
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position; //직급


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @Column(nullable= false)
    private LocalDate hireDate;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy="employee", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Vacation> vacations;

    public Employee(SignupRequest request, String encodedPassword, Position position, UserRole role, Company company, Department department) {
        accountId = request.getAccountId();
        password = encodedPassword;
        userName = request.getUserName();
        email = request.getEmail();
        phoneNumber = request.getPhoneNumber();
        address = request.getAddress();
        hireDate = request.getHireDate();
        this.position = position;
        this.role = role;
        this.company = company;
        this.department = department;
    }

    public Employee(InviteSignupRequest request, String encodedPassword, Position position, UserRole role, Company company, Department department) {
        accountId = request.getAccountId();
        password = encodedPassword;
        userName = request.getUserName();
        email = request.getEmail();
        phoneNumber = request.getPhoneNumber();
        address = request.getAddress();
        hireDate = request.getHireDate();
        this.position = position;
        this.role = role;
        this.company = company;
        this.department = department;
    }

    public void updateRefreshToken(String newRefreshToken) {
        refreshToken = newRefreshToken;
    }

    public void updateProfile(EmployeeRequest request, Position position, Department department, UserRole role) {
        userName = request.getUserName();
        email = request.getEmail();
        phoneNumber = request.getPhoneNumber();
        address = request.getAddress();
        this.position = position;
        this.role = role;
        this.department = department;
    }
}