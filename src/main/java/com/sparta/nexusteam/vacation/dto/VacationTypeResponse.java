package com.sparta.nexusteam.vacation.dto;

import com.sparta.nexusteam.vacation.entity.VacationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class VacationTypeResponse {
    private Long id;
    private String name;
    private int days;

    public VacationTypeResponse(VacationType vacationType) {
        this.id = vacationType.getId();
        this.name = vacationType.getName();
        this.days = vacationType.getDays();
    }
}
