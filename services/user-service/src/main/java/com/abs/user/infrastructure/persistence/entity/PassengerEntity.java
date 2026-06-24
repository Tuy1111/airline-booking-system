package com.abs.user.infrastructure.persistence.entity;

import com.abs.user.domain.vo.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "passenger")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

    @Column(length = 3)
    private String nationality;
}
