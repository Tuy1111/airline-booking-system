package com.abs.user.infrastructure.persistence.entity;

import com.abs.user.domain.vo.Gender;
import com.abs.user.domain.vo.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * JPA persistence model for the passenger profile row. Shares its primary key with {@code users}
 * via {@link MapsId} (one-to-one), so it is always persisted/removed together with its owning user.
 */
@Entity
@Table(name = "passenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "passport_no", length = 20)
    private String passportNo;

    @Column(name = "passport_country", length = 3)
    private String passportCountry;

    @Column(name = "passport_expiry")
    private LocalDate passportExpiry;

    @Column(length = 3)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus;
}
