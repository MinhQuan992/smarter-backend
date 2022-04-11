package com.example.smarterbackend.framework.dto.questiongroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddQuestionGroupPayload {
  @NotBlank(message = "The question group name is required")
  private String name;
}
