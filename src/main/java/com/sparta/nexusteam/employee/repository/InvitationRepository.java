package com.sparta.nexusteam.employee.repository;

import com.sparta.nexusteam.employee.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByToken(String token);
}
