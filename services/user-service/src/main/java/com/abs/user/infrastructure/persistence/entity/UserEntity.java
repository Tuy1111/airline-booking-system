package com.abs.user.infrastructure.persistence.entity;

import com.abs.user.domain.vo.FrequentFlyerTier;
import com.abs.user.domain.vo.Role;
import com.abs.user.domain.vo.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * JPA persistence model for a user. This is an infrastructure detail: it is a flat, primitive-typed
 * row mapping that knows nothing about the domain invariants. The {@code UserPersistenceMapper}
 * translates between this entity and the rich {@code UserAggregate}.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", nullable = false, length = 20)
    private FrequentFlyerTier loyaltyTier;

    @Column(name = "loyalty_miles", nullable = false)
    private long loyaltyMiles;

    @Column(name = "loyalty_lifetime_miles", nullable = false)
    private long loyaltyLifetimeMiles;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private PassengerEntity passenger;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
