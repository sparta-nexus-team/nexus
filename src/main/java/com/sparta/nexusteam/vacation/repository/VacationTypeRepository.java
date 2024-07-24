package com.sparta.nexusteam.vacation.repository;

import com.sparta.nexusteam.vacation.entity.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationTypeRepository extends JpaRepository<VacationType, Long> {

}
