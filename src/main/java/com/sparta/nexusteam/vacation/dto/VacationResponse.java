package com.sparta.nexusteam.vacation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class VacationResponse {
    private Long id;
    private String vacationTypeName;
    private int vacationTypeDays;
    private String employeeUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    private ApprovalStatus approvalStatus;
}
