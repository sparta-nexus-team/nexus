package com.sparta.nexusteam.vacation.repository;

import com.sparta.nexusteam.vacation.entity.VacationTypeHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationTypeHistoryRepository extends JpaRepository<VacationTypeHistory, Long> {
    Optional<VacationTypeHistory> findTopByVacationTypeIdOrderByIdDesc(Long vacationTypeId);
}