package com.example.smarterbackend.framework.dto.user;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import com.example.smarterbackend.framework.validator.CommonPatternConstraint;
import com.example.smarterbackend.framework.validator.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProfilePayload {
  @NotBlank(message = "The name is required")
  @Size(max = 100, message = "The length of the name must not be greater than 100 characters")
  private String name;

  @CommonPatternConstraint(
      regex = RegexConstants.URL_PATTERN,
      message = "The image URL must be valid")
  private String imageUrl;

  @CommonPatternConstraint(message = "The gender must be valid")
  private String gender;

  @CommonPatternConstraint(
      regex = RegexConstants.DATE_PATTERN,
      message = "The birthdate must be valid")
  private String birthdate;

  @PasswordConstraint(
      message =
          "The new password must contain at least one lowercase letter, one uppercase letter and one number; the length of the new password must be between 8 and 20 characters")
  private String newPassword;

  private String confirmedPassword;
}
