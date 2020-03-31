package com.adamsimon.core.repository;

import com.adamsimon.core.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository <Users, Long> {
    public List<Users> findAllByUserId(Long userId);

    public Optional<Users> findByToken(String token);

    public Optional<Users> findById(Long id);

    public Optional<Users> findByName(String name);

    public Optional<Users> findByEmail(String email);

    public void deleteByEmail(String email);

    public void deleteByUserId(Long userId);

    public void deleteById(Long id);
}
