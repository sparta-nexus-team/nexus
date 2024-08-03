package com.sparta.nexusteam.vacation.entity;


import com.sparta.nexusteam.employee.entity.Company;
import com.sparta.nexusteam.vacation.dto.PutVacationTypeRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name="vacation_type")
@NoArgsConstructor
@AllArgsConstructor
public class VacationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=15, nullable = false)
    private String name;

    @Column(nullable = false)
    private int days;

    @JoinColumn(name="company_id",nullable=false)
    @ManyToOne
    private Company company;

    public VacationType(String name, int days, Company company) {
        this.name = name;
        this.days = days;
        this.company = company;
    }

    public void updateVacationType(PutVacationTypeRequest requestDto) {
        this.name=requestDto.getName();
        this.days=requestDto.getDays();
    }
}
