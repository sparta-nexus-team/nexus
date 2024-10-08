package com.sparta.nexusteam.employee.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    private String email;
    private String token;
    private Date expiryDate;
    private String companyName;

    public Invitation(String email, String token, Date expiryDate, String companyName) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
        this.companyName = companyName;
    }
}