package com.example.smarterbackend.framework.dto.user;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyInfoPayload {
  @NotBlank(message = "The name is required")
  @Size(max = 100, message = "The length of the name must not be greater than 100 characters")
  private String name;

  @NotBlank(message = "The email is required")
  @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "The email must be valid")
  private String email;
}
