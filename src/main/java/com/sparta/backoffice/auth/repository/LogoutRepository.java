package com.sparta.backoffice.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.backoffice.auth.entity.Logout;

public interface LogoutRepository extends JpaRepository<Logout, String> {
	void deleteAllByModifiedAtBefore(LocalDateTime localDateTime);
}
