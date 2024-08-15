package com.sparta.nexusteam.work.entity;

import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration workedTime;
    private Duration overtime; // 초과 근무 시간을 저장하는 필드

    @ManyToOne
    private Employee employee;


    public Work(Employee employee, LocalDate date, LocalDateTime startTime) {
        this.employee = employee;
        this.date = date;
        this.startTime = startTime;
    }

    public Work() {

    }
}
