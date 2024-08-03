package com.sparta.nexusteam.work.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkRequest;
import com.sparta.nexusteam.work.dto.WorkResponse;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.entity.SalaryType;
import com.sparta.nexusteam.work.repository.WorkRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;

    public WorkServiceImpl(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    //근무 요청
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "workDay", key = "#employee.id"),
            @CacheEvict(value = "workWeek", key = "#employee.id"),
            @CacheEvict(value = "workMonth", key = "#employee.id")
    })
    public Long saveWork(Employee employee,WorkRequest workRequest){
        SalaryType salaryType = workRequest.getSalary_type();
        Duration work_time = workRequest.getWork_time();
        String message = workRequest.getMessage();
        Work work = new Work(employee,salaryType,message,work_time);
        workRepository.save(work);
        return work.getId();
    }

    //근무 수근
    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "workDay", key = "#employee.id"),
            @CachePut(value = "workWeek", key = "#employee.id"),
            @CachePut(value = "workMonth", key = "#employee.id")
    })
    public Long updateWork(Employee employee,Date date ,WorkRequest workRequest) {
        Work work = workRepository.findByEmployeeAndWorkDate(employee,date);
        work.update(workRequest);
        return work.getId();
    }
    //근무 삭제
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "workDay", key = "#employee.id"),
            @CacheEvict(value = "workWeek", key = "#employee.id"),
            @CacheEvict(value = "workMonth", key = "#employee.id")
    })
    public String deleteWork(Employee employee, Date date) {
        Work work =workRepository.findByEmployeeAndWorkDate(employee,date);
        workRepository.delete(work);
        return "삭제 완료";
    }

    //회원 근무 당일 조회
    @Override
    @Transactional
    @Cacheable(value = "workDay", key = "#employee.id")
    public Page<WorkResponse> getDayWork(Employee employee, Pageable pageable){
        // 현재 날짜를 LocalDate로 가져오기
        LocalDate localDate = LocalDate.now();

        // LocalDate를 ZonedDateTime으로 변환
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());

        // ZonedDateTime을 Date로 변환
        Date today = Date.from(zonedDateTime.toInstant());
        Page<Work> workPages = workRepository.findWorkByToday(pageable, employee.getId(),today);

        return workPages.map(work -> new WorkResponse(work));
    }

    //회원 근무 주간 조회
    @Override
    @Transactional
    @Cacheable(value = "workWeek", key = "#employee.id")
    public Page<WorkResponse> getWeekWork(Employee employee, Pageable pageable) {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages= workRepository.findWorkByDateRange(pageable,employee.getId(),startDate,endDate);

        return workPages.map(work-> new WorkResponse(work));
    }

    //회원 근무 월간 조회
    @Transactional
    @Cacheable(value = "workMonth", key = "#employee.id")
    public Page<WorkResponse> getMonthWork(Employee employee,Pageable pageable) {
        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages = workRepository.findWorkByDateRange(pageable,employee.getId(),startDate,endDate);
        return workPages.map(work-> new WorkResponse(work));
    }
    //직원 근무 당일 조회
    @Override
    @Transactional
    @Cacheable(value = "memberWorkDay", key = "#company_id")
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

    //직원 근무 주간 조회
    @Override
    @Transactional
    @Cacheable(value = "memberWorkWeek", key = "#company_id")
    public Page<WorkResponse> getMemberWeekWork(Long company_id,Pageable pageable){
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Work> workPages = workRepository.findWorkByCompanyAndDateRange(pageable,company_id,startDate,endDate);
        return workPages.map(work->new WorkResponse(work));
    }

    //직원 근무 월간 조회
    @Override
    @Transactional
    @Cacheable(value = "memberWorkMonth", key = "#company_id")
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
