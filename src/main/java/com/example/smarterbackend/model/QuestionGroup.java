package com.example.smarterbackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions;

  public void addQuestion(Question question) {
    this.questions.add(question);
    question.setGroup(this);
  }

  public void removeQuestion(Question question) {
    question.setGroup(null);
    this.questions.remove(question);
  }

  public void removeQuestions() {
    Iterator<Question> iterator = this.questions.iterator();
    while (iterator.hasNext()) {
      Question question = iterator.next();
      question.setGroup(null);
      iterator.remove();
    }
  }
}
