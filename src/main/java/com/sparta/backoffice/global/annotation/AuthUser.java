package com.sparta.backoffice.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

// 런타임 까지 유지
@Retention(RetentionPolicy.RUNTIME)
// 타겟은 파라미터에만
@Target(ElementType.PARAMETER)

// 익명 사용자인 경우에는 null로, 익명 사용자가 아닌 경우에는 실제 CustomuUserDetails의 User 객체로
// Principal 을 동적으로 꺼내기 위해 @AuthUser 생성
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : user")
public @interface AuthUser {
}
