package com.sparta.nexusteam.salary.service;

import com.sparta.nexusteam.salary.dto.SalaryRequestDto;
import com.sparta.nexusteam.salary.entity.Salary;
import com.sparta.nexusteam.salary.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        salary.setMealExpense(BigDecimal.valueOf(150000)); //식비

        BigDecimal grossSalary = BigDecimal.valueOf(totalMonthlySalary).add(BigDecimal.valueOf(150000));

        salary.setIncomeTax(calculateIncomeTax(grossSalary)); // 예시로 고정값 설정
        salary.setLocalIncomeTax(grossSalary.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_UP)); // 예시로 고정값 설정
        salary.setNationalPension(grossSalary.multiply(BigDecimal.valueOf(0.045)).setScale(2, RoundingMode.HALF_UP)); // 예시로 고정값 설정
        salary.setHealthInsurance(grossSalary.multiply(BigDecimal.valueOf(0.03495)).setScale(2, RoundingMode.HALF_UP)); // 예시로 고정값 설정
        salary.setLongTermCareInsurance(grossSalary.multiply(BigDecimal.valueOf(0.011)).setScale(2, RoundingMode.HALF_UP)); // 예시로 고정값 설정
        salary.setEmploymentInsurance(grossSalary.multiply(BigDecimal.valueOf(0.009)).setScale(2, RoundingMode.HALF_UP)); // 예시로 고정값 설정

        return salaryRepository.save(salary);
    }

    public Salary getSalaryById(Long salaryId) {
        Optional<Salary> salary = salaryRepository.findById(salaryId);
        return salary.orElseThrow(() -> new RuntimeException("Salary not found for id: " + salaryId));
    }

    private BigDecimal calculateIncomeTax(BigDecimal grossSalary) {
        BigDecimal incomeTax = BigDecimal.ZERO;

        if (grossSalary.compareTo(BigDecimal.valueOf(12000000)) <= 0) {
            incomeTax = grossSalary.multiply(BigDecimal.valueOf(0.06));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(46000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(grossSalary.subtract(BigDecimal.valueOf(12000000)).multiply(BigDecimal.valueOf(0.15)));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(88000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(46000000)).multiply(BigDecimal.valueOf(0.24)));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(150000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(BigDecimal.valueOf(42000000).multiply(BigDecimal.valueOf(0.24)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(88000000)).multiply(BigDecimal.valueOf(0.35)));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(300000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(BigDecimal.valueOf(42000000).multiply(BigDecimal.valueOf(0.24)))
                    .add(BigDecimal.valueOf(62000000).multiply(BigDecimal.valueOf(0.35)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(150000000)).multiply(BigDecimal.valueOf(0.38)));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(500000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(BigDecimal.valueOf(42000000).multiply(BigDecimal.valueOf(0.24)))
                    .add(BigDecimal.valueOf(62000000).multiply(BigDecimal.valueOf(0.35)))
                    .add(BigDecimal.valueOf(150000000).multiply(BigDecimal.valueOf(0.38)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(300000000)).multiply(BigDecimal.valueOf(0.40)));
        } else if (grossSalary.compareTo(BigDecimal.valueOf(1000000000)) <= 0) {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(BigDecimal.valueOf(42000000).multiply(BigDecimal.valueOf(0.24)))
                    .add(BigDecimal.valueOf(62000000).multiply(BigDecimal.valueOf(0.35)))
                    .add(BigDecimal.valueOf(150000000).multiply(BigDecimal.valueOf(0.38)))
                    .add(BigDecimal.valueOf(200000000).multiply(BigDecimal.valueOf(0.40)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(500000000)).multiply(BigDecimal.valueOf(0.42)));
        } else {
            incomeTax = BigDecimal.valueOf(12000000).multiply(BigDecimal.valueOf(0.06))
                    .add(BigDecimal.valueOf(34000000).multiply(BigDecimal.valueOf(0.15)))
                    .add(BigDecimal.valueOf(42000000).multiply(BigDecimal.valueOf(0.24)))
                    .add(BigDecimal.valueOf(62000000).multiply(BigDecimal.valueOf(0.35)))
                    .add(BigDecimal.valueOf(150000000).multiply(BigDecimal.valueOf(0.38)))
                    .add(BigDecimal.valueOf(200000000).multiply(BigDecimal.valueOf(0.40)))
                    .add(BigDecimal.valueOf(500000000).multiply(BigDecimal.valueOf(0.42)))
                    .add(grossSalary.subtract(BigDecimal.valueOf(1000000000)).multiply(BigDecimal.valueOf(0.45)));
        }

        return incomeTax.setScale(2, RoundingMode.HALF_UP);
    }
}