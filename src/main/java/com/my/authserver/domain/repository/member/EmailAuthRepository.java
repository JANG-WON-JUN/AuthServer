package com.my.authserver.domain.repository.member;


import com.my.authserver.domain.entity.member.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
}
