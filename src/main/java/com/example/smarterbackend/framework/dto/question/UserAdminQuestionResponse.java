package com.example.smarterbackend.framework.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminQuestionResponse {
  private Long questionId;
  private String shortContent;
  private String imageUrl;
  private boolean isAnswered;
  private boolean isAnswerCorrect;
  private boolean isFavorite;
}
