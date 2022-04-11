package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.AddUserPayload;
import com.example.smarterbackend.framework.dto.user.VerificationResponse;
import com.example.smarterbackend.framework.dto.user.VerifyInfoPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/users")
@Validated
public interface UserAPI {
  @PostMapping("/signup/verify-info")
  ResponseEntity<VerificationResponse> verifyInfo(
      @Valid @RequestBody VerifyInfoPayload payload);

  @PostMapping("/signup/add-user")
  ResponseEntity<UserResponse> addUser(@Valid @RequestBody AddUserPayload payload);

  @GetMapping("/current-user")
  ResponseEntity<UserResponse> getCurrentUser();
}
