package com.sparta.nexusteam.employee.dto;

import com.sparta.nexusteam.employee.entity.Department;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.entity.Position;
import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private Position position;
    private Department department;

    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.userName = employee.getUserName();
        this.email = employee.getEmail();
        this.phoneNumber = employee.getPhoneNumber();
        this.position = employee.getPosition();
        this.department = employee.getDepartment();
    }
}
