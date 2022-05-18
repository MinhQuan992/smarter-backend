package com.example.smarterbackend.framework.dto.question;

import com.example.smarterbackend.framework.common.data.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
  private Long id;
  private String content;
  private String answerA;
  private String answerB;
  private String answerC;
  private String answerD;
  private Answer correctAnswer;
  private String imageUrl;
  private String information;
  private String reference;
  private Long groupId;
  private boolean isFavorite;
}
