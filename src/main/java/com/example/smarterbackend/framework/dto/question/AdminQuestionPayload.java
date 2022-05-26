package com.example.smarterbackend.framework.dto.question;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminQuestionPayload extends BaseQuestionPayload {
  @NotBlank(message = "The ID of question group is required")
  @Pattern(
      regexp = RegexConstants.COMMON_ID_PATTERN,
      message = "The ID must contain numeric characters only")
  private String groupId;
}
