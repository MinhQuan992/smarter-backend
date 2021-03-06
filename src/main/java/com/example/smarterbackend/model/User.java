package com.example.smarterbackend.model;

import com.example.smarterbackend.framework.common.data.Gender;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private Gender gender;

  private LocalDate dateOfBirth;

  private String imageUrl;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "authority_id")
  private Authority authority;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserAdminQuestion> answeredQuestions;

  public void addAnsweredQuestions(UserAdminQuestion question) {
    this.answeredQuestions.add(question);
  }

  public void removeQuestion(UserAdminQuestion question) {
    question.setUser(null);
    this.answeredQuestions.remove(question);
  }

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserQuestion> userQuestions;

  public void addUserQuestions(UserQuestion question) {
    this.userQuestions.add(question);
    question.setAuthor(this);
  }

  public void removeQuestion(UserQuestion question) {
    question.setAuthor(null);
    this.userQuestions.remove(question);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(authority);
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
