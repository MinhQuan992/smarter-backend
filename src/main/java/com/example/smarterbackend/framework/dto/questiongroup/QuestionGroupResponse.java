package com.example.smarterbackend.framework.dto.questiongroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupResponse {
  private Long id;
  private String name;
}
