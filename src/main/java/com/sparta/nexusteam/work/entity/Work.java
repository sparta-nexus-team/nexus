package com.sparta.nexusteam.work.entity;

import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Date;
import java.util.List;

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
    private WorkStatus work_status;

    private String message;

    @Enumerated(EnumType.STRING)
    private AllowedStatus allowed_status = AllowedStatus.WORK_NOT_ALLOWED;


    public Work(Employee employee,WorkStatus work_status, String message,Duration work_time) {
        this.employee = employee;
        this.work_status = work_status;
        this.message = message;
        this.work_time = work_time;
    }
    @PrePersist
    protected void onCreate(){
        this.work_date = new Date();
    }
}
