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
public class AddUserPayload {
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

  @NotBlank(message = "The OTP is required")
  @Pattern(regexp = RegexConstants.OTP_PATTERN, message = "The OTP must contain numeric characters only")
  @Size(min = 6, max = 6, message = "The length of the OTP must be 6 characters")
  private String otp;
}
