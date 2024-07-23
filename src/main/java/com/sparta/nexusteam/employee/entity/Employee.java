package com.sparta.nexusteam.employee.entity;

import com.sparta.nexusteam.employee.dto.SignupRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountId; //로그인 아이디

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{8,}$") //소문자,숫자,특수기호,8자이상
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

    @Column(nullable = false)
    private String position; //직급


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String refreshToken;

    public Employee(SignupRequest request, String encodedPassword, UserRole role) {
        accountId = request.getAccountId();
        password = encodedPassword;
        userName = request.getUserName();
        email = request.getEmail();
        phoneNumber = request.getPhoneNumber();
        address = request.getAddress();
        this.role = role;
    }

    public void updateRefreshToken(String newRefreshToken) {
        refreshToken = newRefreshToken;
    }
}