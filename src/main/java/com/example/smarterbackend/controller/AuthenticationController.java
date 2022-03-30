package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.AuthenticationAPI;
import com.example.smarterbackend.framework.dto.authentication.AuthenticationResponse;
import com.example.smarterbackend.framework.dto.authentication.CredentialPayload;
import com.example.smarterbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationAPI {
  private final AuthenticationService authenticationService;

  @Override
  public ResponseEntity<AuthenticationResponse> login(CredentialPayload payload) {
    return ResponseEntity.ok(authenticationService.authenticate(payload));
  }
}
