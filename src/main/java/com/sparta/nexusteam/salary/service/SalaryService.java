package com.sparta.nexusteam.salary.service;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.employee.repository.EmployeeRepository;
import com.sparta.nexusteam.salary.entity.Salary;
import com.sparta.nexusteam.salary.repository.SalaryRepository;
import com.sparta.nexusteam.work.service.WorkService;
import com.sparta.nexusteam.work.service.WorkServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SalaryService {


    private final SalaryRepository salaryRepository;

    private  final WorkServiceImpl workService;

    private final EmployeeRepository employeeRepository;

    public List<Salary> calculateAndSaveMonthlySalary() {
        List<Employee> employees = employeeRepository.findAll();
        Map<Employee, Duration> overtimeMap = workService.calculateMonthlyOvertime(); // 각 사원의 초과 근무 시간 계산

        List<Salary> salaries = new ArrayList<>();

        for (Employee employee : employees) {
            Duration monthlyOvertime = overtimeMap.getOrDefault(employee, Duration.ZERO); // 초과 근무 시간 가져오기
            Salary salary = makeSalary(employee, monthlyOvertime); // 초과 근무 시간을 반영한 급여 계산
            salaryRepository.save(salary); // 급여 정보 저장
            salaries.add(salary); // 결과 리스트에 추가
        }

        return salaries;
    }

    public List<Salary> getSalaryById(Employee employee) {
        return salaryRepository.findAllByEmployeeId(employee.getId());
    }

    private Salary makeSalary(Employee employee, Duration overtime){
        double hourlyWage = 10000.0;

        // 월 기본 급여 계산
        double monthlyBaseSalary = employee.getWage();

        double over_time = overtime.toMinutes();
        // 월 연장 근무 수당 계산
        double overtimePay = over_time * hourlyWage * 1.5;

        // 총 월급 계산
        double totalMonthlySalary = monthlyBaseSalary + overtimePay;

        Salary salary = new Salary();
        salary.setEmployeeId(employee.getId());
        salary.setPayDate(LocalDateTime.now());
        salary.setBasicSalary(BigDecimal.valueOf(monthlyBaseSalary));
        salary.setFixedOvertimeAllowance(BigDecimal.valueOf(overtimePay));
        salary.setMealExpense(BigDecimal.valueOf(150000)); //식비

        BigDecimal grossSalary = BigDecimal.valueOf(totalMonthlySalary).add(BigDecimal.valueOf(150000));

        salary.setIncomeTax(calculateIncomeTax(grossSalary)); // 예시로 고정값 설정
        salary.setLocalIncomeTax(grossSalary.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_UP));
        salary.setNationalPension(grossSalary.multiply(BigDecimal.valueOf(0.045)).setScale(2, RoundingMode.HALF_UP));
        salary.setHealthInsurance(grossSalary.multiply(BigDecimal.valueOf(0.03495)).setScale(2, RoundingMode.HALF_UP));
        salary.setLongTermCareInsurance(grossSalary.multiply(BigDecimal.valueOf(0.011)).setScale(2, RoundingMode.HALF_UP));
        salary.setEmploymentInsurance(grossSalary.multiply(BigDecimal.valueOf(0.009)).setScale(2, RoundingMode.HALF_UP));

        // 총 공제 금액 계산
        BigDecimal totalDeductions = salary.getIncomeTax()
                .add(salary.getLocalIncomeTax())
                .add(salary.getNationalPension())
                .add(salary.getHealthInsurance())
                .add(salary.getLongTermCareInsurance())
                .add(salary.getEmploymentInsurance());

        // 실수령액 계산
        BigDecimal amountWage = grossSalary.subtract(totalDeductions);
        salary.setAmountWage(amountWage);

        return salary;
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