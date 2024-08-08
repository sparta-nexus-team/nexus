package com.sparta.nexusteam.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveResponseDto {
    private Long employeeId;
    private String employeeUserName;
    private int totalAnnualVacation;
    private int usedAnnualVacation;
    private int remainingAnnualVacation;
}
