package com.sparta.nexusteam.vacation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.Vacation;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VacationResponse {
    private Long id;
    private String vacationTypeName;
    private String employeeUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    private ApprovalStatus approvalStatus;

    public VacationResponse(Vacation vacation) {
        this.id = vacation.getId();
        if(vacation.getVacationTypeHistory() != null) {
            this.vacationTypeName = vacation.getVacationTypeHistory().getName();
        } else {
            this.vacationTypeName = "연차";
        }
        this.employeeUserName = vacation.getEmployee().getUserName();
        this.startDate = vacation.getStartDate();
        this.endDate = vacation.getEndDate();
        this.approvalStatus = vacation.getApprovalStatus();
    }
}