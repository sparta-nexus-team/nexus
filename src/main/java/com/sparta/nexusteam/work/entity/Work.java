package com.sparta.nexusteam.work.entity;

import com.sparta.nexusteam.employee.entity.Employee;
import com.sparta.nexusteam.work.dto.WorkRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
public class Work {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    private Duration work_time;

    @Temporal(TemporalType.DATE)
    @Column(unique = true)
    private Date work_date;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;

    private String message;

    @Enumerated(EnumType.STRING)
    private AllowedStatus allowed_status = AllowedStatus.WORK_NOT_ALLOWED;


    public Work(Employee employee, SalaryType salaryType, String message, Duration work_time) {
        this.employee = employee;
        this.salaryType = salaryType;
        this.message = message;
        this.work_time = work_time;
    }
    public void update(WorkRequest request){
        this.salaryType = request.getSalary_type();
        this.message = request.getMessage();
        this.work_time = request.getWork_time();
    }
    @PrePersist
    protected void onCreate(){
        this.work_date = new Date();
    }
}
