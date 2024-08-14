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
    private Date workDate;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;


    public Work(Employee employee, SalaryType salaryType, Duration work_time) {
        this.employee = employee;
        this.salaryType = salaryType;
        this.work_time = work_time;
    }
    public void update(WorkRequest request){
        //기준 설정 시간
        Duration eightHours = Duration.ofHours(8);
        Duration eightAndHalfHours = Duration.ofHours(8).plusMinutes(30);

        this.work_time = request.getWork_time();

        if(request.getWork_time().compareTo(eightHours) <= 0){
            //근무시간이 8시간 이하인 경우
            this.salaryType = SalaryType.VACATION;
        }else if (work_time.compareTo(eightAndHalfHours) <= 0){
            this.salaryType = SalaryType.CUSTOMIZED_WORK;
        }else{
            this.salaryType = SalaryType.OVER_WORK;
        }

    }
    @PrePersist
    protected void onCreate(){
        this.workDate = new Date();
    }
}
