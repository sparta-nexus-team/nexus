package com.sparta.nexusteam.vacation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.nexusteam.employee.entity.QEmployee;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.QVacation;
import com.sparta.nexusteam.vacation.entity.QVacationType;
import com.sparta.nexusteam.vacation.entity.QVacationTypeHistory;
import com.sparta.nexusteam.vacation.entity.Vacation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VacationRepositoryCustomImpl implements VacationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QVacationType vacationType = QVacationType.vacationType;
    QVacationTypeHistory vacationTypeHistory = QVacationTypeHistory.vacationTypeHistory;
    QVacation vacation = QVacation.vacation;
    QEmployee employee = QEmployee.employee;


    @Override
    public List<Vacation> findByCompanyIdAndApprovalStatus(Long companyId,
            ApprovalStatus approvalStatus) {

        return queryFactory.select(vacation)
                .from(vacation)
                .join(vacation.employee, employee)
                .where(employee.company.id.eq(companyId)
                        .and(vacation.approvalStatus.eq(approvalStatus)))
                .orderBy(vacation.startDate.asc())
                .fetch();
    }

    @Override
    public List<Vacation> findUsedAnnualLeaveAfterGrantDate(LocalDateTime GrantDateTime,
            Long employeeId) {
        return queryFactory.select(vacation)
                .from(vacation)
                .where(vacation.isAnnualLeave.eq(true)
                        .and(vacation.employee.id.eq(employeeId))
                        .and(vacation.startDate.after(GrantDateTime))
                )
                .fetch();
    }
}