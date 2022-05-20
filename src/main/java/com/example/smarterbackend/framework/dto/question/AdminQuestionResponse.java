package com.example.smarterbackend.framework.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AdminQuestionResponse extends BaseQuestionResponse {
  private Long groupId;
  private boolean isFavorite;
}
