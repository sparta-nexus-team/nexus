package com.sparta.nexusteam.work.service;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkRequest;
import com.sparta.nexusteam.work.dto.WorkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkService {
    Long saveWork(Employee employee,WorkRequest workRequest);

    Page<WorkResponse> getWeekWork(Employee employee, Pageable pageable);
    Page<WorkResponse> getMemberDayWork(Long company_id,Pageable pageable);
}
