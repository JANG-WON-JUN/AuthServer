package com.my.authserver.member.web;

import static com.my.authserver.common.web.dto.Result.of;
import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.authserver.common.web.dto.Result;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.service.PasswordService;
import com.my.authserver.member.service.query.PasswordQueryService;
import com.my.authserver.member.web.request.PasswordUpdateRequest;
import com.my.authserver.member.web.response.PasswordResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class PasswordController {

	private final PasswordService passwordService;
	private final PasswordQueryService passwordQueryService;

	@GetMapping("/api/password/{id}")
	public ResponseEntity<Result<PasswordResponse>> findPassword(@PathVariable Long id) {
		Password savedPassword = passwordQueryService.findById(id);
		PasswordResponse response = PasswordResponse.of(savedPassword);
		return ok(of(response));
	}

	@PatchMapping("/api/password")
	public ResponseEntity<Result<Long>> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
		Long savedPasswordId = passwordService.updatePassword(request.toServiceRequest());
		return ok(of(savedPasswordId));
	}

}