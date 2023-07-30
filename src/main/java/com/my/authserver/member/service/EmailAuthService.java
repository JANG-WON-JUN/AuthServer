package com.my.authserver.member.service;

import static java.util.Locale.*;
import static org.springframework.util.StringUtils.*;

import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.my.authserver.common.web.exception.dto.SendMailFail;
import com.my.authserver.config.aop.annotaion.Retry;
import com.my.authserver.domain.entity.member.EmailAuth;
import com.my.authserver.member.repository.EmailAuthRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

	private final JavaMailSender javaMailSender;
	private final EmailAuthRepository emailAuthRepository;
	private final MessageSource messageSource;

	public void createEmailAuth(EmailAuth emailAuth) {
		emailAuthRepository.save(emailAuth);
	}

	public EmailAuth findById(Long id) {
		return emailAuthRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	@Retry
	public void sendAuthMail(String receiverEmail, String authCode) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(receiverEmail);
			mimeMessageHelper.setSubject("Community 회원가입 인증메일입니다.");
			mimeMessageHelper.setText(getEmailAuthContent(authCode));

			javaMailSender.send(mimeMessage);
		} catch (MailException | MessagingException e) {
			throw new SendMailFail(messageSource.getMessage("error.sendMail", null, getDefault()));
		}
	}

	private String getEmailAuthContent(String authCode) throws MessagingException {
		if (!hasText(authCode)) {
			throw new MessagingException();
		}

		return "회원가입을 위한 인증코드입니다. \n"
			+ "아래의 인증코드를 입력해 주세요. \n\n"
			+ "인증코드 : " + authCode;
	}
}
