package com.example.smarterbackend.model;

import com.example.smarterbackend.framework.common.data.Gender;
import com.example.smarterbackend.framework.common.data.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private Gender gender;

  private LocalDate dateOfBirth;

  private String imageUrl;

  @Column(nullable = false)
  private Role role;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;
}
