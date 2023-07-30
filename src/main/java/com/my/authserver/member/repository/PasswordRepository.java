package com.my.authserver.member.repository;

import com.my.authserver.domain.entity.member.Password;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
