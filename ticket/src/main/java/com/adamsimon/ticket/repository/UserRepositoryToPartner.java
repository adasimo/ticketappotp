package com.adamsimon.ticket.repository;

import com.adamsimon.ticket.domain.UserToPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryToPartner extends JpaRepository<UserToPartner, Long> {
    @Override
    Optional<UserToPartner> findById(Long id);
    Optional<UserToPartner> findByToken(String token);
}
