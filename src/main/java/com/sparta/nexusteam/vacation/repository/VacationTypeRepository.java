package com.sparta.nexusteam.vacation.repository;

import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.vacation.entity.VacationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationTypeRepository extends JpaRepository<VacationType, Long> {
    List<VacationType> findByCompanyIdAndIsDeletedFalse(Long CompanyId);
}