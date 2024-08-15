package com.sparta.nexusteam.work.dto;

import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.entity.SalaryType;
import lombok.Getter;

import java.time.Duration;
import java.util.Date;
@Getter
public class WorkResponse {
    private String work_time;

    private Date work_date;

    private SalaryType salary_type;


    public WorkResponse(Work work) {
        this.work_time = work.getWork_time().toHours()+" : "+work.getWork_time().toMinutes()%60;
        this.work_date = work.getWorkDate();
        this.salary_type = work.getSalaryType();

    }
}
