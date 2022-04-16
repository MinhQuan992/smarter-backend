package com.example.smarterbackend.model;

import com.example.smarterbackend.model.compositekey.UserQuestionId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserQuestion {
  @EmbeddedId
  private UserQuestionId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("questionId")
  private Question question;

  @Column(nullable = false)
  private boolean isCorrect;

  @Column(nullable = false)
  private boolean isFavorite;

  public UserQuestion(User user, Question question, boolean isCorrect, boolean isFavorite) {
    this.user = user;
    this.question = question;
    this.isCorrect = isCorrect;
    this.isFavorite = isFavorite;
    this.id = new UserQuestionId(user.getId(), question.getId());
  }
}
