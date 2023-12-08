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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "logouts",
	indexes = @Index(name = "idx_logout_access_token", columnList = "accessToken")
)
@Entity

public class Logout extends BaseEntity {
	@Id
	private String accessToken;
	
	@Column
	private String username;

	public static Logout of(String accessToken, String username) {
		return new Logout(accessToken, username);
	}
}
