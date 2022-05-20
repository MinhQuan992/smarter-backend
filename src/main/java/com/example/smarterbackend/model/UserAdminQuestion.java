package com.example.smarterbackend.model;

import com.example.smarterbackend.model.compositekey.UserAdminQuestionId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserAdminQuestion {
  @EmbeddedId private UserAdminQuestionId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("adminQuestionId")
  private AdminQuestion adminQuestion;

  @Column(nullable = false)
  private boolean isCorrect;

  @Column(nullable = false)
  private boolean isFavorite;

  public UserAdminQuestion(
      User user, AdminQuestion adminQuestion, boolean isCorrect, boolean isFavorite) {
    this.user = user;
    this.adminQuestion = adminQuestion;
    this.isCorrect = isCorrect;
    this.isFavorite = isFavorite;
    this.id = new UserAdminQuestionId(user.getId(), adminQuestion.getId());
  }
}
