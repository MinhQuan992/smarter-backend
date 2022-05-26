package com.example.smarterbackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<AdminQuestion> adminQuestions;

  public void addQuestion(AdminQuestion adminQuestion) {
    this.adminQuestions.add(adminQuestion);
    adminQuestion.setGroup(this);
  }

  public void removeQuestion(AdminQuestion adminQuestion) {
    adminQuestion.setGroup(null);
    this.adminQuestions.remove(adminQuestion);
  }

  public void removeQuestions() {
    Iterator<AdminQuestion> iterator = this.adminQuestions.iterator();
    while (iterator.hasNext()) {
      AdminQuestion adminQuestion = iterator.next();
      adminQuestion.setGroup(null);
      iterator.remove();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    QuestionGroup that = (QuestionGroup) o;
    return id.equals(that.id) && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
