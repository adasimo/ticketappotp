package com.adamsimon.core.repository;

import com.adamsimon.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository <User, Long> {
    public List<User> findAllByUserId(final Long userId);

    public Optional<User> findByToken(final String token);

    public Optional<User> findByUserId(final Long id);

    public Optional<User> findByName(final String name);

    public Optional<User> findByEmail(final String email);

    public void deleteByEmail(final String email);

    public void deleteByUserId(final Long userId);

}
