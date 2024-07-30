package com.sparta.nexusteam.salary.controller;

import com.sparta.nexusteam.salary.dto.SalaryRequestDto;
import com.sparta.nexusteam.salary.entity.Salary;
import com.sparta.nexusteam.salary.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @GetMapping("/{salary_id}")
    public Salary getSalary(@PathVariable("salary_id") Long salaryId) {
        return salaryService.getSalaryById(salaryId);
    }

    @PostMapping("/calculate")
    public Salary calculateSalary(@RequestBody SalaryRequestDto requestDto) {
        return salaryService.calculateAndSaveMonthlySalary(requestDto);
    }
}