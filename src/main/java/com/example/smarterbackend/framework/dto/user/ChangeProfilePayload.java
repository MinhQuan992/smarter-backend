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
public class ChangeProfilePayload {
  @NotBlank(message = "The name is required")
  @Size(max = 100, message = "The length of the name must not be greater than 100 characters")
  private String name;

  @Pattern(regexp = RegexConstants.URL_PATTERN, message = "The image URL must be valid")
  private String imageUrl;

  @Pattern(regexp = RegexConstants.GENDER_PATTERN, message = "The gender must be male or female")
  private String gender;

  @Pattern(regexp = RegexConstants.DATE_PATTERN, message = "The birthdate must be valid")
  private String birthdate;

  @Pattern(
      regexp = RegexConstants.PASSWORD_PATTERN,
      message =
          "The new password must contain at least one lowercase letter, one uppercase letter and one number")
  @Size(
      min = 8,
      max = 20,
      message = "The length of the new password must be between 8 and 20 characters")
  private String newPassword;

  private String confirmedPassword;
}
