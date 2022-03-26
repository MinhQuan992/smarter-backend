package com.example.smarterbackend.model;

import com.example.smarterbackend.framework.common.data.Answer;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 1000, nullable = false)
  private String content;

  @Column(length = 800, nullable = false)
  private String answerA;

  @Column(length = 800, nullable = false)
  private String answerB;

  @Column(length = 800)
  private String answerC;

  @Column(length = 800)
  private String answerD;

  @Column(nullable = false)
  private Answer correctAnswer;

  private String imageUrl;

  @Column(nullable = false)
  private String information;

  @Column(nullable = false)
  private String reference;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private QuestionGroup group;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Question question = (Question) o;
    return id.equals(question.id)
        && content.equals(question.content)
        && answerA.equals(question.answerA)
        && answerB.equals(question.answerB)
        && Objects.equals(answerC, question.answerC)
        && Objects.equals(answerD, question.answerD)
        && correctAnswer == question.correctAnswer
        && Objects.equals(imageUrl, question.imageUrl)
        && information.equals(question.information)
        && reference.equals(question.reference)
        && group.equals(question.group);
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
