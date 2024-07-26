package com.sparta.nexusteam.vacation.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public VacationType(String name, int days) {
        this.name = name;
        this.days = days;
    }
}
