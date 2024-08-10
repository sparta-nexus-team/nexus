package com.sparta.nexusteam.vacation.entity;

import com.sparta.nexusteam.employee.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vacation")
@NoArgsConstructor
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus;

    @Column(name= "annual_leave", nullable = false)
    private Boolean isAnnualLeave;

    @JoinColumn(name="vacation_type_history_id")
    @ManyToOne
    private VacationTypeHistory vacationTypeHistory;

    @JoinColumn(name="employee_id",nullable = false)
    @ManyToOne
    private Employee employee;

    public Vacation(LocalDateTime startDate, LocalDateTime endDate, VacationTypeHistory vacationTypeHistory,
                    Employee employee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.vacationTypeHistory = vacationTypeHistory;
        this.employee = employee;
        this.isAnnualLeave = false;
    }

    public Vacation(LocalDateTime startDate, LocalDateTime endDate, Employee employee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.employee = employee;
        this.isAnnualLeave = true;
    }

    public void updateApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}