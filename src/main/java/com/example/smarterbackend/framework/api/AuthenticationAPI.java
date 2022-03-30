package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.authentication.AuthenticationResponse;
import com.example.smarterbackend.framework.dto.authentication.CredentialPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/authentication")
@Validated
public interface AuthenticationAPI {
  @PostMapping("/login")
  ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid CredentialPayload payload);
}
