package com.example.smarterbackend.model.compositekey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionId implements Serializable {
  @Column
  Long userId;

  @Column
  Long questionId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserQuestionId that = (UserQuestionId) o;
    return userId.equals(that.userId) && questionId.equals(that.questionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, questionId);
  }
}
