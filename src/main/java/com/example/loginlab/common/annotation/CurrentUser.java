package com.example.loginlab.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @CurrentUser
 * 현재 인증된 사용자의 이메일을 가져오기 위한 어노테이션
 */

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface CurrentUser {

}
