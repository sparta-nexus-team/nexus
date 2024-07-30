package com.sparta.nexusteam.salary.service;

import com.sparta.nexusteam.salary.dto.SalaryRequestDto;
import com.sparta.nexusteam.salary.entity.Salary;
import com.sparta.nexusteam.salary.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public Salary calculateAndSaveMonthlySalary(SalaryRequestDto requestDto) {
        double hourlyWage = requestDto.getHourlyWage();
        double dailyWorkingHours = requestDto.getDailyWorkingHours();
        int weeklyWorkingDays = requestDto.getWeeklyWorkingDays();
        double monthlyOvertimeHours = requestDto.getMonthlyOvertimeHours();
        boolean includeWeeklyAllowance = requestDto.isIncludeWeeklyAllowance();

        // 한 주의 총 근무 시간 계산
        double weeklyWorkingHours = dailyWorkingHours * weeklyWorkingDays;

        // 한 달의 총 근무 시간 계산 (4주 기준)
        double monthlyWorkingHours = weeklyWorkingHours * 4;

        // 월 기본 급여 계산
        double monthlyBaseSalary = monthlyWorkingHours * hourlyWage;

        // 월 연장 근무 수당 계산
        double overtimePay = monthlyOvertimeHours * hourlyWage * 1.5;

        // 주휴 수당 계산
        double weeklyAllowance = 0;
        if (includeWeeklyAllowance && weeklyWorkingHours >= 15) {
            weeklyAllowance = (weeklyWorkingHours / weeklyWorkingDays) * hourlyWage * 4;
        }

        // 총 월급 계산
        double totalMonthlySalary = monthlyBaseSalary + overtimePay + weeklyAllowance;

        // Salary 엔티티 생성 및 저장
        Salary salary = new Salary();
        salary.setEmployeeId(requestDto.getEmployeeId());
        salary.setPayDate(LocalDateTime.now());
        salary.setBasicSalary(BigDecimal.valueOf(monthlyBaseSalary));
        salary.setFixedOvertimeAllowance(BigDecimal.valueOf(overtimePay));
        salary.setMealExpense(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setIncomeTax(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setLocalIncomeTax(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setNationalPension(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setHealthInsurance(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setLongTermCareInsurance(BigDecimal.ZERO); // 예시로 고정값 설정
        salary.setEmploymentInsurance(BigDecimal.ZERO); // 예시로 고정값 설정

        return salaryRepository.save(salary);
    }

    public Salary getSalaryById(Long salaryId) {
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        return salary.orElseThrow(() -> new RuntimeException("Salary not found for id: " + salaryId));
    }
}