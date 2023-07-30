package com.my.authserver.member.repository;

import com.my.authserver.domain.entity.member.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
