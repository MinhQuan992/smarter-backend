package com.example.smarterbackend.framework.dto.question;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPayload {
  @NotBlank(message = "The content is required")
  @Size(max = 1000, message = "The length of the content must not exceed 1000 characters")
  private String content;

  @NotBlank(message = "The answer A is required")
  @Size(max = 800, message = "The length of the answer A must not exceed 800 characters")
  private String answerA;

  @NotBlank(message = "The answer B is required")
  @Size(max = 800, message = "The length of the answer B must not exceed 800 characters")
  private String answerB;

  @Size(max = 800, message = "The length of the answer C must not exceed 800 characters")
  private String answerC;

  @Size(max = 800, message = "The length of the answer D must not exceed 800 characters")
  private String answerD;

  @NotBlank(message = "The correct answer is required")
  private String correctAnswer;

  private String imageUrl;

  @NotBlank(message = "The information is required")
  private String information;

  @NotBlank(message = "The reference is required")
  private String reference;

  @NotBlank(message = "The ID of question group is required")
  @Pattern(
      regexp = RegexConstants.COMMON_ID_PATTERN,
      message = "The ID must contain numeric characters only")
  private String groupId;
}
