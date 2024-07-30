package com.sparta.nexusteam.work.service;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkRequest;
import com.sparta.nexusteam.work.dto.WorkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface WorkService {
    Long saveWork(Employee employee,WorkRequest workRequest);
    Long updateWork(Employee employee, Date date, WorkRequest workRequest);
    Page<WorkResponse> getDayWork(Employee employee,Pageable pageable);

    Page<WorkResponse> getWeekWork(Employee employee, Pageable pageable);
    Page<WorkResponse> getMemberDayWork(Long company_id,Pageable pageable);
    Page<WorkResponse> getMemberWeekWork(Long company_id,Pageable pageable);
    Page<WorkResponse> getMemberMonthWork(Long company_id,Pageable pageable);
}
