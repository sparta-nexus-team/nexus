package com.sparta.nexusteam.salary.controller;

import com.sparta.nexusteam.base.CommonResponse;
import com.sparta.nexusteam.salary.entity.Salary;
import com.sparta.nexusteam.salary.service.SalaryService;
import com.sparta.nexusteam.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.nexusteam.base.ControllerUtil.getBadRequestResponseEntity;
import static com.sparta.nexusteam.base.ControllerUtil.getResponseEntity;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @GetMapping
    public ResponseEntity<CommonResponse> getSalary(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try{
            List<Salary> response = salaryService.getSalaryById(userDetails.getEmployee());
            return getResponseEntity(response, "급여 조회 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<CommonResponse> calculateSalary() {
        try{
            List<Salary> response = salaryService.calculateAndSaveMonthlySalary();
            return getResponseEntity(response, "급여 계산 성공");
        } catch (Exception e) {
            return getBadRequestResponseEntity(e);
        }
    }
}