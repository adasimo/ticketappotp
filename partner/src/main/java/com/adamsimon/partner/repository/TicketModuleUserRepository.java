package com.adamsimon.partner.repository;

import com.adamsimon.partner.domain.TicketModuleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketModuleUserRepository extends JpaRepository<TicketModuleUser, Long> {
    @Override
    Optional<TicketModuleUser> findById(Long id);
    Optional<TicketModuleUser> findByToken(String token);
}
