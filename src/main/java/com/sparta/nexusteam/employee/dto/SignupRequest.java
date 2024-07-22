package com.sparta.nexusteam.employee.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String accountId;
    private String password;
    private String userName;
    private String adminToken;
    private boolean admin;
}
