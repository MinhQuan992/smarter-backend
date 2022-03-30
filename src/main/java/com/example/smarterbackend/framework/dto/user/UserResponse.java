package com.example.smarterbackend.framework.dto.user;

import com.example.smarterbackend.framework.common.data.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long id;
  private String name;
  private Gender gender;
  private LocalDate dateOfBirth;
  private String imageUrl;
  private String email;
  private String password;
}
