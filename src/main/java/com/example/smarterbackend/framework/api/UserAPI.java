package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.UserSignUpWithoutOtpParams;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/users")
@Validated
public interface UserAPI {
  ResponseEntity<UserResponse> signUpWithoutOtp(
      @Valid @RequestBody UserSignUpWithoutOtpParams payload);
}
