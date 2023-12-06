package com.sparta.backoffice.user.repository;

import com.sparta.backoffice.user.entity.PasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findTop3ByUserIdOrderByModifiedAtDesc (Long userId, Pageable pageable);

}
