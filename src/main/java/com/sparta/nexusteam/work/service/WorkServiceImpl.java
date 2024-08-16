package com.sparta.nexusteam.work.service;


import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.repository.WorkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Map<String, String>> toggleWork(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();  // Employee 객체를 가져옴
        LocalDate today = LocalDate.now();
        Optional<Work> existingWork = workRepository.findByEmployeeAndDate(employee, today);

        Map<String, String> response = new HashMap<>();

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

                workRepository.save(work);
                response.put("status", "workEnded");
                response.put("message", "퇴근 기록이 성공적으로 저장되었습니다.");
            } else {
                throw new IllegalStateException("이미 오늘 퇴근 기록이 있습니다.");
            }
        } else {
            Work newWork = new Work(employee, today, LocalDateTime.now());
            workRepository.save(newWork);
            response.put("status", "workStarted");
            response.put("message", "출근 기록이 성공적으로 저장되었습니다.");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> getFormattedWorkedTime(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();
        LocalDate today = LocalDate.now();
        Work work = workRepository.findByEmployeeAndDate(employee, today)
                .orElseThrow(() -> new IllegalStateException("오늘의 출퇴근 기록이 없습니다."));

        Map<String, String> response = new HashMap<>();
        if (work.getWorkedTime() == null) {
            response.put("message", "퇴근 기록 없음");
        } else {
            response.put("총 일한 시간", formatDuration(work.getWorkedTime()));
            response.put("초과 근무 시간", formatDuration(work.getOvertime()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<Map<String, String>>> getWorkedTimeByDate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Employee employee = userDetails.getEmployee();
        List<Work> works = workRepository.findAll().stream()
                .filter(work -> work.getEmployee().equals(employee))
                .collect(Collectors.toList());

        List<Map<String, String>> responseList = works.stream()
                .map(work -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("date", work.getDate().toString());
                    response.put("일한 시간", work.getWorkedTime() != null
                            ? formatDuration(work.getWorkedTime())
                            : "퇴근 기록 없음");
                    response.put("초과 근무 시간", work.getOvertime() != null
                            ? formatDuration(work.getOvertime())
                            : "없음");
                    return response;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public ResponseEntity<List<Map<String, String>>> getWorkDetailsByDate(LocalDate date) {
        List<Work> works = workRepository.findAllByDate(date);

        List<Map<String, String>> responseList = works.stream()
                .map(work -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("사원", work.getEmployee().getUserName());
                    response.put("일한 시간", work.getWorkedTime() != null
                            ? formatDuration(work.getWorkedTime())
                            : "퇴근 기록 없음");
                    response.put("초과 근무 시간", work.getOvertime() != null
                            ? formatDuration(work.getOvertime())
                            : "없음");
                    return response;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
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
