package com.sparta.backoffice.auth.entity;

import com.sparta.backoffice.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
	name = "refresh_tokens",
	indexes = @Index(name = "idx_refresh_token_refreshToken", columnList = "token")
)
@Entity
public class RefreshToken extends BaseEntity {
	@Id
	private String username;

	@Column
	private String token;

	public static RefreshToken of(String username, String token) {
		return new RefreshToken(username, token);
	}
}
