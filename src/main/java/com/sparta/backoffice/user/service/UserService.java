package com.sparta.backoffice.user.service;

import com.sparta.backoffice.user.dto.request.PasswordUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.ProfileUpdateRequestDto;
import com.sparta.backoffice.user.dto.request.UserDetailsRequestDto;
import com.sparta.backoffice.user.dto.response.PasswordUpdateResponseDto;
import com.sparta.backoffice.user.dto.response.ProfileUpdateResponseDto;
import com.sparta.backoffice.user.entity.PasswordHistory;
import com.sparta.backoffice.user.entity.User;
import com.sparta.backoffice.user.repository.PasswordHistoryRepository;
import com.sparta.backoffice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ProfileUpdateResponseDto updateProfile(Long userId, ProfileUpdateRequestDto requestDto, UserDetailsRequestDto userDetailsRequestDto) {
        User user = foundUser(userId);

        checkUserPermission(user, userDetailsRequestDto);

        String newNickname = requestDto.getNickname();
        if(!newNickname.equals(user.getNickname()) && userRepository.existsByNickname(newNickname)){  // 기존닉네임과 같은지 , 닉네임이 중복인지
            throw new IllegalArgumentException("닉네임을 변경할 수 없습니다.");
        }

        User newProfile = user.updateProfile(requestDto);

        ProfileUpdateResponseDto responseDto = new ProfileUpdateResponseDto(newProfile);

        return responseDto;
    }

    @Transactional
    public PasswordUpdateResponseDto updatePassword(Long userId, PasswordUpdateRequestDto requestDto, UserDetailsRequestDto userDetailsRequestDto) {
        User user = foundUser(userId);

        checkUserPermission(user, userDetailsRequestDto);

        Pageable recentPasswords = PageRequest.of(0, 3);
        List<PasswordHistory> recentThreePasswords = passwordHistoryRepository.findTop3ByUserIdOrderByModifiedAtDesc(userId, recentPasswords);

        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());    // 비밀번호 암호화 필요

        for (PasswordHistory passwordHistory : recentThreePasswords) {
            if (passwordEncoder.matches(requestDto.getPassword(), passwordHistory.getPassword())) {
                throw new IllegalArgumentException("최근에 사용한 비밀번호입니다.");
            }
        }

        user.updatePassword(encodedNewPassword);
        userRepository.save(user);

        PasswordHistory updatedPasswordHistory = new PasswordHistory(user, encodedNewPassword);
        passwordHistoryRepository.save(updatedPasswordHistory);


        return new PasswordUpdateResponseDto(user);
    }

    private User foundUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UsernameNotFoundException("존재하지 않는 유저입니다."));
    }

    private void checkUserPermission(User user, UserDetailsRequestDto userDetailsRequestDto ) {
        if (!user.getUsername().equals(userDetailsRequestDto.getUsername())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}
