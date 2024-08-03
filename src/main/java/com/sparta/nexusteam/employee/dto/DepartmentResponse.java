package com.sparta.nexusteam.employee.dto;

import lombok.Data;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;

    public DepartmentResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
