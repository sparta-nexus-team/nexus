package com.sparta.nexusteam.salary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary")
@Getter
@Setter
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate;

    @Column(name = "basic_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal basicSalary; // 기본급

    @Column(name = "fixed_overtime_allowance", precision = 10, scale = 2)
    private BigDecimal fixedOvertimeAllowance; // 고정 초과 근무 수당

    @Column(name = "meal_expense", precision = 10, scale = 2)
    private BigDecimal mealExpense; // 식비

    @Column(name = "income_tax", precision = 10, scale = 2)
    private BigDecimal incomeTax; // 소득세

    @Column(name = "local_income_tax", precision = 10, scale = 2)
    private BigDecimal localIncomeTax; // 지방소득세

    @Column(name = "national_pension", precision = 10, scale = 2)
    private BigDecimal nationalPension; // 국민연금

    @Column(name = "health_insurance", precision = 10, scale = 2)
    private BigDecimal healthInsurance; // 건강보험

    @Column(name = "long_term_care_insurance", precision = 10, scale = 2)
    private BigDecimal longTermCareInsurance; // 장기요양보험

    @Column(name = "employment_insurance", precision = 10, scale = 2)
    private BigDecimal employmentInsurance; // 고용보험
}