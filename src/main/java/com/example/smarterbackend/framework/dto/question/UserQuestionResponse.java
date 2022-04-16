package com.example.smarterbackend.framework.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionResponse {
  private Long questionId;
  private String shortContent;
  private boolean isAnswered;
  private boolean isAnswerCorrect;
  private boolean isFavorite;
}
