package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import com.example.smarterbackend.framework.dto.question.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@RequestMapping("/questions")
@Validated
public interface QuestionAPI {
  @GetMapping("/group/{groupId}")
  ResponseEntity<List<QuestionResponse>> getQuestionsByGroup(
      @PathVariable("groupId")
          @NotBlank(message = "The group ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The group ID must contain numeric characters only")
          String groupId);

  @GetMapping("/{questionId}")
  ResponseEntity<QuestionResponse> getQuestionById(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId);

  @PostMapping
  ResponseEntity<QuestionResponse> addQuestion(@Valid @RequestBody QuestionPayload payload);

  @PutMapping("/{questionId}")
  ResponseEntity<QuestionResponse> updateQuestion(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody QuestionPayload payload);

  @PostMapping("/check-answer/{questionId}")
  ResponseEntity<CheckAnswerResponse> checkAnswer(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody CheckAnswerPayload payload);
}
