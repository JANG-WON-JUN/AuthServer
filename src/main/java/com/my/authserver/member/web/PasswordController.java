package com.my.authserver.member.web;

import com.my.authserver.common.web.dto.ApiResponse;
import com.my.authserver.domain.entity.member.Password;
import com.my.authserver.member.service.PasswordService;
import com.my.authserver.member.service.query.PasswordQueryService;
import com.my.authserver.member.web.request.PasswordUpdateRequest;
import com.my.authserver.member.web.response.PasswordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.my.authserver.common.web.dto.ApiResponse.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class PasswordController {

    private final PasswordService passwordService;
    private final PasswordQueryService passwordQueryService;

    @GetMapping("/api/password/{id}")
    public ApiResponse<PasswordResponse> findPassword(@PathVariable Long id) {
        Password savedPassword = passwordQueryService.findById(id);
        PasswordResponse response = PasswordResponse.of(savedPassword);
        return ok(response);
    }

    @PatchMapping("/api/password")
    public ApiResponse<Long> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        Long savedPasswordId = passwordService.updatePassword(request.toServiceRequest());
        return ok(savedPasswordId);
    }

}