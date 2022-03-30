package com.example.smarterbackend.framework.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialPayload {
  @NotBlank(message = "The email is required")
  private String email;

  @NotBlank(message = "The password is required")
  private String password;
}
