package com.sparta.nexusteam.work.controller;


import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.service.WorkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/work")
public class WorkController {

    @Autowired
    private WorkServiceImpl workService;

    @PostMapping("/toggle")
    public String toggleWork(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        workService.toggleWork(userDetails);
        return workService.getFormattedWorkedTime(userDetails);
    }

    @GetMapping("/worked-time-by-date")
    public List<String> getWorkedTimeByDate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return workService.getWorkedTimeByDate(userDetails);
    }

    @GetMapping("/work/date/{date}")
    public List<String> getWorkDetailsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return workService.getWorkDetailsByDate(localDate);
    }
}
