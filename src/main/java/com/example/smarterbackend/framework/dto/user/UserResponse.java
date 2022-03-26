package com.example.smarterbackend.framework.dto.user;

import com.example.smarterbackend.framework.common.data.Gender;
import com.example.smarterbackend.framework.common.data.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
  private Long id;
  private String name;
  private Gender gender;
  private LocalDate dateOfBirth;
  private String imageUrl;
  private Role role;
  private String email;
  private String password;
}
