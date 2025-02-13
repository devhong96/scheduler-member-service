package com.scheduler.memberservice.infra.security.jwt.domain;

import com.scheduler.memberservice.infra.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Date expiryDate;

    public RefreshToken(String userId, String refreshToken, Date expiryDate) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiryDate = expiryDate;
    }
}
