package com.adamsimon.core.repository;

import com.adamsimon.core.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository <Users, Long> {
}
