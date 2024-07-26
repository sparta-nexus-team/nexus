package com.sparta.nexusteam.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "아이디는 빈칸이 될 수 없습니다")
    private String accountId;

    @NotBlank(message = "비밀번호는 빈칸이 될 수 없습니다")
    private String password;

    @NotBlank(message = "이름은 빈칸이 될 수 없습니다")
    private String userName;

    @NotBlank(message = "이메일은 빈칸이 될 수 없습니다")
    @Email(message = "이메일형식을 지켜주세요")
    private String email;

    @NotBlank(message = "전화번호는 빈칸이 될 수 없습니다")
    @Pattern(regexp = "^(01[016789])-?(\\d{3,4})-?(\\d{4})$")
    private String phoneNumber;

    @NotBlank(message = "주소는 빈칸이 될 수 없습니다")
    private String address;

    private String adminToken;
    private boolean admin;
}
