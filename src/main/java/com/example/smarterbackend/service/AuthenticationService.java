package com.example.smarterbackend.service;

import com.example.smarterbackend.framework.common.constant.SecurityConstants;
import com.example.smarterbackend.framework.dto.authentication.AuthenticationResponse;
import com.example.smarterbackend.framework.dto.authentication.CredentialPayload;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse authenticate(CredentialPayload payload) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();
    String accessToken = JwtUtil.generateJwt(user);

    return new AuthenticationResponse(accessToken, SecurityConstants.EXPIRATION_TIME);
  }
}
