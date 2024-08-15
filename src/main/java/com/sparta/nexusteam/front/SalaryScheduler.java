package com.sparta.nexusteam.front;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.service.WorkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Component
public class SalaryScheduler {

    private final RestTemplate restTemplate;

    public SalaryScheduler() {
        this.restTemplate = new RestTemplate();
    }


    // 매월 25일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 25 * ?")
    public void executeSalaryApi() {
        String apiUrl = "/salary/calculate"; // 실제 API URL로 대체하세요
        try {
            restTemplate.getForObject(apiUrl, String.class);
            System.out.println("Salary API executed successfully.");
        } catch (Exception e) {
            System.err.println("Error executing Salary API: " + e.getMessage());
        }
    }
}
