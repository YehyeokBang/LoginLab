package com.example.loginlab.app;

import com.example.loginlab.api.dto.UserDto;
import com.example.loginlab.domain.users.user.User;
import com.example.loginlab.domain.users.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminService adminService;

    List<User> allUsers = new ArrayList<>();

    List<User> testUsers = new ArrayList<>();

    List<User> adminUsers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        allUsers.add(User.builder().email("test@gmail.com").nickname("test").phone("01012345678").build());
        allUsers.add(User.builder().email("admin@gmail.com").build());

        testUsers.add(User.builder().email("test@gmail.com").nickname("test").phone("01012345678").build());

        adminUsers.add(User.builder().email("admin@gmail.com").build());
    }

    @Test
    @DisplayName("관리자가 아무 조건 없이 유저 목록을 조회할 때")
    void getUsersWithoutSearchCondition() {
        // given
        UserDto.UserSearchRequest request = UserDto.UserSearchRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>(allUsers)));

        // when
        Page<UserDto.UserResponse> users = adminService.getUsers(request, pageable);

        // then
        assertEquals(users.getContent().size(), allUsers.size());
        for (int i = 0; i < users.getContent().size(); i++) {
            assertEquals(users.getContent().get(i).getEmail(), allUsers.get(i).getEmail());
        }
    }

    @Test
    @DisplayName("관리자가 이메일로 유저 목록을 조회할 때")
    void getUsersWithEmailSearchCondition() {
        // given
        UserDto.UserSearchRequest request = UserDto.UserSearchRequest.builder().email("test@gmail.com").build();
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(ArgumentMatchers.<Specification<User>>any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>(testUsers)));

        // when
        Page<UserDto.UserResponse> users = adminService.getUsers(request, pageable);

        // then
        assertEquals(users.getContent().size(), 1);
        for (int i = 0; i < users.getContent().size(); i++) {
            assertEquals(users.getContent().get(i).getEmail(), allUsers.get(i).getEmail());
        }
    }

}
