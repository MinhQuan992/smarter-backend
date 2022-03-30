package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.UserSignUpWithOtpPayload;
import com.example.smarterbackend.framework.dto.user.UserSignUpWithoutOtpPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/users")
@Validated
public interface UserAPI {
  @PostMapping("/signup")
  ResponseEntity<UserResponse> signUpWithoutOtp(
      @Valid @RequestBody UserSignUpWithoutOtpPayload payload);

  @PostMapping("/signup/verify")
  ResponseEntity<UserResponse> signUpWithOtp(@Valid @RequestBody UserSignUpWithOtpPayload payload);
}
