package com.sparta.nexusteam.employee.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @ManyToOne
    private Company company;

    public void updateName(String newName) {
        this.name = newName;
    }
}
