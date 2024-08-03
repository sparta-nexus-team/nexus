package com.sparta.nexusteam.work.dto;

import com.sparta.nexusteam.work.entity.SalaryType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Duration;

@Getter
public class WorkRequest {
    @NotNull(message = "근무 상태를 입력하세요!")
    private SalaryType salary_type;
    @NotNull(message = "근무 시간을 입력하세요!")
    private Duration work_time;
    @NotNull(message = "근무 요청 메시지를 입력하세요!")
    private String message;

}
