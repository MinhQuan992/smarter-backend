package com.example.smarterbackend.framework.dto.question;

import com.example.smarterbackend.framework.common.data.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseQuestionResponse {
  protected Long id;
  protected String content;
  protected String answerA;
  protected String answerB;
  protected String answerC;
  protected String answerD;
  protected Answer correctAnswer;
  protected String imageUrl;
  protected String information;
  protected String reference;
}
