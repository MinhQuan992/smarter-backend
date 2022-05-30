package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.user.AddUserPayload;
import com.example.smarterbackend.framework.dto.user.ChangeProfilePayload;
import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.VerifyInfoPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/users")
@Validated
public interface UserAPI {
  @PostMapping("/signup/verify-info")
  ResponseEntity<DynamicResponse> verifyInfo(@Valid @RequestBody VerifyInfoPayload payload);

  @PostMapping("/signup/add-user")
  ResponseEntity<UserResponse> addUser(@Valid @RequestBody AddUserPayload payload);

  @GetMapping("/current-user")
  ResponseEntity<UserResponse> getCurrentUser();

  @PutMapping("/change-profile")
  ResponseEntity<UserResponse> changeProfile(@Valid @RequestBody ChangeProfilePayload payload);
}
