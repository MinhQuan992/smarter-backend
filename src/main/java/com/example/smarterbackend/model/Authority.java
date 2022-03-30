package com.example.smarterbackend.model;

import com.example.smarterbackend.framework.common.data.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Role name;

  @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<User> users;

  @Override
  public String getAuthority() {
    return name.name();
  }
}
