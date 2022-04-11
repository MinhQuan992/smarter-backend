package com.example.smarterbackend.framework.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckAnswerPayload {
  @NotBlank(message = "The chosen answer is required")
  private String chosenAnswer;
}
