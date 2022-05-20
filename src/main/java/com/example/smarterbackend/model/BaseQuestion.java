package com.example.smarterbackend.model;

import com.example.smarterbackend.framework.common.data.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseQuestion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Column(length = 1000, nullable = false)
  protected String content;

  @Column(length = 800, nullable = false)
  protected String answerA;

  @Column(length = 800, nullable = false)
  protected String answerB;

  @Column(length = 800)
  protected String answerC;

  @Column(length = 800)
  protected String answerD;

  @Column(nullable = false)
  protected Answer correctAnswer;

  protected String imageUrl;

  @Type(type = "text")
  protected String information;

  protected String reference;
}
