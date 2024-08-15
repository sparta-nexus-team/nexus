package com.sparta.nexusteam.work.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkService {
    Work toggleWork(@AuthenticationPrincipal UserDetailsImpl userDetails);
    String getFormattedWorkedTime(@AuthenticationPrincipal UserDetailsImpl userDetails);
    List<String> getWorkedTimeByDate(@AuthenticationPrincipal UserDetailsImpl userDetails);
    List<String> getWorkDetailsByDate(LocalDate date);
    Map<Employee, Duration> calculateMonthlyOvertime();
}
