package com.sparta.nexusteam.work.service;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkRequest;
import com.sparta.nexusteam.work.dto.WorkResponse;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.entity.WorkStatus;
import com.sparta.nexusteam.work.repository.WorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;

    public WorkServiceImpl(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }
    @Override
    @Transactional
    public Long saveWork(Employee employee,WorkRequest workRequest){
        WorkStatus workStatus = workRequest.getWork_status();
        Duration work_time = workRequest.getWork_time();
        String message = workRequest.getMessage();
        Work work = new Work(employee,workStatus,message,work_time);
        workRepository.save(work);
        return work.getId();
    }

    @Override
    @Transactional
    public Page<WorkResponse> getWeekWork(Employee employee, Pageable pageable) {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages= workRepository.findWorkByDateRange(pageable,employee.getId(),startDate,endDate);

        return workPages.map(work-> new WorkResponse(work));
    }

    @Override
    public Page<WorkResponse> getMemberDayWork(Long company_id, Pageable pageable) {
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate localDate = LocalDate.now();

        // LocalDate를 ZonedDateTime으로 변환
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());

        // ZonedDateTime을 Date로 변환
        Date today = Date.from(zonedDateTime.toInstant());
        Page<Work> workPages = workRepository.findWorkByCompanyAndToday(pageable,company_id,today);
        return workPages.map(work-> new WorkResponse(work));
    }

    public Page<WorkResponse> getMemberWeekWork(Long company_id,Pageable pageable){
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages = workRepository.findWorkByCompanyAndDateRange(pageable,company_id,startDate,endDate);
        return workPages.map(work->new WorkResponse(work));
    }

    public Page<WorkResponse> getMemberMonthWork(Long company_id,Pageable pageable){
        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages = workRepository.findWorkByCompanyAndDateRange(pageable,company_id,startDate,endDate);
        return workPages.map(work->new WorkResponse(work));
    }
}
