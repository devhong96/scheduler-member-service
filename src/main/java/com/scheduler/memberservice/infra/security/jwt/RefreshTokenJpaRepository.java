package com.scheduler.memberservice.infra.security.jwt;

import com.scheduler.memberservice.infra.security.jwt.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

	@Transactional(readOnly = true)
	boolean existsByRefreshToken(String refreshToken);

	@Transactional
	void deleteByRefreshToken(String refreshToken);

	@Transactional
	void deleteByExpiryDateBefore(Date expiryDate);

	@Transactional(readOnly = true)
	Optional<RefreshToken> findRefreshTokenByUserId(String userId);
}
