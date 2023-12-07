package com.sparta.backoffice.auth.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.backoffice.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	void deleteAllByModifiedAtBefore(LocalDateTime date);
}
