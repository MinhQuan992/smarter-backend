package com.example.smarterbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdminQuestion extends BaseQuestion {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private QuestionGroup group;

  @OneToMany(mappedBy = "adminQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserAdminQuestion> users;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AdminQuestion adminQuestion = (AdminQuestion) o;
    return id.equals(adminQuestion.id)
        && content.equals(adminQuestion.content)
        && answerA.equals(adminQuestion.answerA)
        && answerB.equals(adminQuestion.answerB)
        && Objects.equals(answerC, adminQuestion.answerC)
        && Objects.equals(answerD, adminQuestion.answerD)
        && correctAnswer == adminQuestion.correctAnswer
        && Objects.equals(imageUrl, adminQuestion.imageUrl)
        && information.equals(adminQuestion.information)
        && reference.equals(adminQuestion.reference)
        && group.equals(adminQuestion.group);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        content,
        answerA,
        answerB,
        answerC,
        answerD,
        correctAnswer,
        imageUrl,
        information,
        reference,
        group);
  }
}
