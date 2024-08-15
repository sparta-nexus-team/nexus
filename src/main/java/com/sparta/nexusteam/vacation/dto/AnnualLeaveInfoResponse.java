package com.sparta.nexusteam.vacation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveInfoResponse {
    private Long employeeId;
    private String employeeUserName;
    private float totalAnnualLeave;
    private float usedAnnualLeave;
    private float remainingAnnualLeave;
}
