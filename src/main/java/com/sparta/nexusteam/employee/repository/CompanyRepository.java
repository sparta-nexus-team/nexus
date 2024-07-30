package com.sparta.nexusteam.employee.repository;

import com.sparta.nexusteam.employee.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String companyName);

    Company findByName(String companyName);
}
