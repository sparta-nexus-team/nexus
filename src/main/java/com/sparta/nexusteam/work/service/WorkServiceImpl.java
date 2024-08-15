package com.sparta.nexusteam.work.service;


import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.repository.WorkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workRepository;

    private static final Duration REGULAR_WORK_HOURS = Duration.ofHours(9);
    private static final Duration MINIMUM_WORK_HOURS = Duration.ofHours(8);
    private static final Duration EXTRA_HOUR = Duration.ofHours(1);

    public Work toggleWork(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();  // Employee 객체를 가져옴
        LocalDate today = LocalDate.now();
        Optional<Work> existingWork = workRepository.findByEmployeeAndDate(employee, today);

        if (existingWork.isPresent()) {
            Work work = existingWork.get();
            if (work.getEndTime() == null) {
                work.setEndTime(LocalDateTime.now());
                Duration workedTime = Duration.between(work.getStartTime(), work.getEndTime());

                // 자정을 넘어가는 경우 초과 근무 시간을 0으로 설정
                if (!work.getStartTime().toLocalDate().equals(work.getEndTime().toLocalDate())) {
                    work.setOvertime(Duration.ZERO);
                } else {
                    if (workedTime.compareTo(MINIMUM_WORK_HOURS) >= 0 && workedTime.compareTo(REGULAR_WORK_HOURS) <= 0) {
                        work.setWorkedTime(MINIMUM_WORK_HOURS);
                        work.setOvertime(workedTime.minus(MINIMUM_WORK_HOURS).minus(EXTRA_HOUR));
                    } else if (workedTime.compareTo(REGULAR_WORK_HOURS) > 0) {
                        work.setWorkedTime(MINIMUM_WORK_HOURS);
                        work.setOvertime(workedTime.minus(MINIMUM_WORK_HOURS).minus(EXTRA_HOUR));
                    } else {
                        work.setWorkedTime(workedTime);
                        work.setOvertime(Duration.ZERO);
                    }
                }

                return workRepository.save(work);
            } else {
                throw new IllegalStateException("이미 오늘 퇴근 기록이 있습니다.");
            }
        } else {
            Work newWork = new Work(employee, today, LocalDateTime.now());
            return workRepository.save(newWork);
        }
    }

    public String getFormattedWorkedTime(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();
        LocalDate today = LocalDate.now();
        Work work = workRepository.findByEmployeeAndDate(employee, today)
                .orElseThrow(() -> new IllegalStateException("오늘의 출퇴근 기록이 없습니다."));

        if (work.getWorkedTime() == null) {
            return "퇴근 기록 없음";
        }

        return String.format("총 일한 시간: %s, 초과 근무 시간: %s",
                formatDuration(work.getWorkedTime()),
                formatDuration(work.getOvertime()));
    }

    public List<String> getWorkedTimeByDate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();
        List<Work> works = workRepository.findAll().stream()
                .filter(work -> work.getEmployee().equals(employee))
                .collect(Collectors.toList());

        return works.stream()
                .map(work -> {
                    String date = work.getDate().toString();
                    String workedTime = work.getWorkedTime() != null
                            ? formatDuration(work.getWorkedTime())
                            : "퇴근 기록 없음";
                    String overtime = work.getOvertime() != null
                            ? formatDuration(work.getOvertime())
                            : "없음";
                    return String.format("%s - 일한 시간: %s, 초과 근무 시간: %s", date, workedTime, overtime);
                })
                .collect(Collectors.toList());
    }

    public List<String> getWorkDetailsByDate(LocalDate date) {
        List<Work> works = workRepository.findAllByDate(date);

        return works.stream()
                .map(work -> {
                    String employeeName = work.getEmployee().getUserName(); // Employee 이름
                    String workedTime = work.getWorkedTime() != null
                            ? formatDuration(work.getWorkedTime())
                            : "퇴근 기록 없음";
                    String overtime = work.getOvertime() != null
                            ? formatDuration(work.getOvertime())
                            : "없음";
                    return String.format("사원: %s, 일한 시간: %s, 초과 근무 시간: %s", employeeName, workedTime, overtime);
                })
                .collect(Collectors.toList());
    }

    public Map<Employee, Duration> calculateMonthlyOvertime() {
        LocalDate startDate = YearMonth.now().minusMonths(1).atDay(26); // 이전 달 26일
        LocalDate endDate = YearMonth.now().atDay(25); // 이번 달 25일

        List<Work> works = workRepository.findByDateBetween(startDate, endDate);
        Map<Employee, Duration> overtimeMap = new HashMap<>();

        for (Work work : works) {
            Duration overtime = work.getOvertime();
            if (overtime != null && !overtime.isNegative()) {
                Employee employee = work.getEmployee();
                overtimeMap.put(employee, overtimeMap.getOrDefault(employee, Duration.ZERO).plus(overtime));
            }
        }

        return overtimeMap;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
