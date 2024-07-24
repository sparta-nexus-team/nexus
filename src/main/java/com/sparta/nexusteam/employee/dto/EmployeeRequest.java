package com.sparta.nexusteam.employee.dto;

import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Position;
import com.sparta.nexusteam.employee.entity.UserRole;
import lombok.Data;

@Data
public class EmployeeRequest {
    private String userName;
    private String email;
    private String phoneNumber;
    private String address;
    private Position position;
    private Department department;
    private UserRole role;
}
