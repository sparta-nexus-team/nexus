package com.sparta.nexusteam.work.dto;

import com.sparta.nexusteam.work.entity.AllowedStatus;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.entity.SalaryType;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
@Getter
public class WorkResponse {
    private Duration work_time;

    private Date work_date;

    private SalaryType salary_type;

    private String message;

    private AllowedStatus allowed_status;

    public WorkResponse(Work work) {
        this.work_time = work.getWork_time();
        this.work_date = work.getWork_date();
        this.salary_type = work.getSalaryType();
        this.message = work.getMessage();
        this.allowed_status = work.getAllowed_status();
    }
}
