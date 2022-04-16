package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.UserAPI;
import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.AddUserPayload;
import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.user.VerifyInfoPayload;
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
  public ResponseEntity<DynamicResponse> verifyInfo(VerifyInfoPayload payload) {
    return ResponseEntity.ok(userService.verifyInfoAndGenerateOTP(payload));
  }

  @Override
  public ResponseEntity<UserResponse> addUser(AddUserPayload payload) {
    return new ResponseEntity<>(userService.addUser(payload), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<UserResponse> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUserDTO());
  }
}
