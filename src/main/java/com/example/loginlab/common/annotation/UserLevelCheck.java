package com.example.loginlab.common.annotation;

import com.example.loginlab.domain.users.common.UserLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.example.loginlab.domain.users.common.UserLevel.UNAUTH;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface UserLevelCheck {
    UserLevel level() default UNAUTH;
}
