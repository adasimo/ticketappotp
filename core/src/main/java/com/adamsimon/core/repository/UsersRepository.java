package com.adamsimon.core.repository;

import com.adamsimon.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository <User, Long> {
    public Optional<User> findByToken(final String token);
}
