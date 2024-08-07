package com.sparta.nexusteam.vacation.repository;

import static com.sparta.nexusteam.vacation.entity.QVacationTypeHistory.vacationTypeHistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.nexusteam.vacation.entity.ApprovalStatus;
import com.sparta.nexusteam.vacation.entity.QVacation;
import com.sparta.nexusteam.vacation.entity.QVacationType;
import com.sparta.nexusteam.vacation.entity.Vacation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VacationRepositoryCustomImpl implements VacationRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Vacation> findByCompanyIdAndApprovalStatus(Long companyId, ApprovalStatus approvalStatus){
        QVacationType vacationType = QVacationType.vacationType;
        QVacation vacation = QVacation.vacation;

        return queryFactory.select(vacation)
                .from(vacation)
                .join(vacation.vacationTypeHistory, vacationTypeHistory)
                .join(vacationTypeHistory.vacationType, vacationType)
                .where(vacationType.company.id.eq(companyId).and(vacation.approvalStatus.eq(approvalStatus)))
                .orderBy(vacation.startDate.asc())
                .fetch();
    }
}
