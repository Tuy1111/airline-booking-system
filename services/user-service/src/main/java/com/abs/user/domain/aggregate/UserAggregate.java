package com.abs.user.domain.aggregate;

import com.abs.user.domain.vo.Role;
import com.abs.user.domain.vo.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAggregate {
    private Long id;
    private String email;
    private String passwordHash;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private PassengerAggregate passenger;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
