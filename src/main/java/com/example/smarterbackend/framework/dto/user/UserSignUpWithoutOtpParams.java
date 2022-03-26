package com.example.smarterbackend.framework.dto.user;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserSignUpWithoutOtpParams {
  @NotBlank(message = "The name is required")
  @Size(max = 100, message = "The length of the name must not be greater than 100 characters")
  private String name;

  @NotBlank(message = "The email is required")
  @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "The email must be valid")
  private String email;

  @NotBlank(message = "The password is required")
  @Pattern(
      regexp = RegexConstants.PASSWORD_PATTERN,
      message =
          "The password must contain at least one lowercase letter, one uppercase letter and one number")
  @Size(
      min = 8,
      max = 20,
      message = "The length of the password must be between 8 and 20 characters")
  private String password;
}
