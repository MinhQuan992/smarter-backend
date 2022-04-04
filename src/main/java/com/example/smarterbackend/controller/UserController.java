package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.UserAPI;
import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.UserSignUpWithOtpPayload;
import com.example.smarterbackend.framework.dto.user.UserSignUpWithoutOtpPayload;
import com.example.smarterbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {
  private final UserService userService;

  @Override
  public ResponseEntity<UserResponse> signUpWithoutOtp(UserSignUpWithoutOtpPayload payload) {
    return ResponseEntity.ok(userService.validateInfoAndGenerateOTP(payload));
  }

  @Override
  public ResponseEntity<UserResponse> signUpWithOtp(UserSignUpWithOtpPayload payload) {
    return new ResponseEntity<>(userService.addNewUser(payload), HttpStatus.CREATED);
  }
}
