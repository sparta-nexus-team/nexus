package com.sparta.nexusteam.work.controller;


import com.sparta.nexusteam.security.UserDetailsImpl;
import com.sparta.nexusteam.work.entity.Work;
import com.sparta.nexusteam.work.service.WorkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work")
public class WorkController {

    @Autowired
    private WorkServiceImpl workService;

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, String>> toggleWork(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return workService.toggleWork(userDetails);
    }

    @GetMapping("/worked-time-by-date")
    public ResponseEntity<List<Map<String, String>>> getWorkedTimeByDate(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return workService.getWorkedTimeByDate(userDetails);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Map<String, String>>> getWorkDetailsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return workService.getWorkDetailsByDate(localDate);
    }
}
