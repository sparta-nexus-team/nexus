package com.sparta.nexusteam.vacation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="vacation_type_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VacationTypeHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String name;

    @Column(nullable=false)
    private int days;

    @JoinColumn(nullable=false)
    @ManyToOne
    private VacationType vacationType;


    public VacationTypeHistory(VacationType vacationType) {
        this.name = vacationType.getName();
        this.days = vacationType.getDays();
        this.vacationType = vacationType;
    }
}
