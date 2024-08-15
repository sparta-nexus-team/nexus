package com.sparta.nexusteam.work.dto;

import com.sparta.nexusteam.work.entity.SalaryType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Duration;

@Getter
public class WorkRequest {
    @NotNull(message = "근무 시간을 입력하세요!")
    private Duration work_time;

}
