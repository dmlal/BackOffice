package com.sparta.backoffice.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.backoffice.auth.repository.LogoutRepository;
import com.sparta.backoffice.auth.repository.RefreshTokenRepository;
import com.sparta.backoffice.global.util.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthScheduleService {
	private final RefreshTokenRepository refreshTokenRepository;
	private final LogoutRepository logoutRepository;
	private final JwtProvider jwtProvider;

	//매일 0시 0분 0초 -> refresh 토큰 유효기간 지난 row 삭제
	@Transactional
	@Scheduled(cron="0 0 0 * * *")
	public void refreshTokenSchedule() {
		LocalDateTime expireDate = LocalDateTime.now().minus(jwtProvider.getRefreshTokenExpiration(), ChronoUnit.MILLIS);
		refreshTokenRepository.deleteAllByModifiedAtBefore(expireDate);

		log.debug("delete refreshToken history -> today : {}", expireDate);
	}

	//매일 0시 0분 0초 -> access 토큰 유효기간 지난 row 삭제
	@Transactional
	@Scheduled(cron="0 0 0 * * *")
	public void logoutSchedule() {
		LocalDateTime expireDate = LocalDateTime.now().minus(jwtProvider.getAccessTokenExpiration(), ChronoUnit.MILLIS);
		logoutRepository.deleteAllByModifiedAtBefore(expireDate);

		log.debug("delete logout history -> today : {}", expireDate);
	}
}
