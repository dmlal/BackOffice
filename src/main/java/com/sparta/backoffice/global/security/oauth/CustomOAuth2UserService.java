package com.sparta.backoffice.global.security.oauth;

import static com.sparta.backoffice.global.constant.SocialType.*;

import java.util.Optional;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sparta.backoffice.global.constant.SocialType;
import com.sparta.backoffice.global.security.CustomUserDetails;
import com.sparta.backoffice.global.security.oauth.user.OAuth2UserDetails;
import com.sparta.backoffice.global.security.oauth.user.OAuth2UserInfoFactory;
import com.sparta.backoffice.user.constant.UserRoleEnum;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.UserRepository;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "oauth2 인증 서비스")
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	public CustomOAuth2UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@SneakyThrows
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oauth2Userrequest) throws
		OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(oauth2Userrequest);

		try {
			return processOAuth2User(oauth2Userrequest, oauth2User);
		} catch (Exception exception) {
			log.error("OAuth2 Authentication process error");
			throw new InternalAuthenticationServiceException(exception.getMessage(), exception.getCause());
		}
	}

	private OAuth2User processOAuth2User(
		OAuth2UserRequest oauth2Userrequest, OAuth2User oauth2User) {
		String registrationId = oauth2Userrequest.getClientRegistration().getRegistrationId().toUpperCase();
		SocialType socialType = valueOf(registrationId);

		OAuth2UserDetails oAuth2UserDetails = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType,
			oauth2User.getAttributes());
		return new CustomUserDetails(findUser(socialType, oAuth2UserDetails), oauth2User.getAttributes());
	}

	private User findUser(SocialType socialType, OAuth2UserDetails oauth2Userdetails) {
		Optional<User> userOptional = switch (socialType) {
			case NAVER -> userRepository.findByNaverId(oauth2Userdetails.getId());
			case KAKAO -> userRepository.findByKakaoId(oauth2Userdetails.getId());
		};

		return userOptional.orElseGet(() -> createUser(socialType, oauth2Userdetails));
	}

	private User createUser(SocialType socialType, OAuth2UserDetails oauth2Userdetails) {
		String username = switch (socialType) {
			case KAKAO -> socialType.getCode() + oauth2Userdetails.getId();
			case NAVER -> socialType.getCode() + oauth2Userdetails.getId().substring(0, 10);
		};

		User user = new User(
			username,
			"NO_PASSWORD",
			UserRoleEnum.USER
		);

		switch (socialType) {
			case KAKAO -> user.setKakaoId(oauth2Userdetails.getId());
			case NAVER -> user.setNaverId((oauth2Userdetails.getId()));
		}
		return userRepository.save(user);
	}
}