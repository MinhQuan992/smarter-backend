package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import com.example.smarterbackend.framework.dto.DynamicResponse;
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
  ResponseEntity<List<AdminQuestionResponse>> getQuestionsByGroup(
      @PathVariable("groupId")
          @NotBlank(message = "The group ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The group ID must contain numeric characters only")
          String groupId);

  @GetMapping("/{questionId}")
  ResponseEntity<AdminQuestionResponse> getQuestionById(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId);

  @PostMapping
  ResponseEntity<AdminQuestionResponse> addQuestion(
      @Valid @RequestBody AdminQuestionPayload payload);

  @PutMapping("/update-question/{questionId}")
  ResponseEntity<AdminQuestionResponse> updateQuestion(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody AdminQuestionPayload payload);

  @GetMapping("/random-question")
  ResponseEntity<AdminQuestionResponse> getRandomQuestion();

  @GetMapping("/next-question")
  ResponseEntity<AdminQuestionResponse> getNextQuestionInGroup(
      @RequestParam("currentQuestionId")
          @NotBlank(message = "The current question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The current question ID must contain numeric characters only")
          String currentQuestionId,
      @RequestParam("getCurrent") boolean getCurrent);

  @GetMapping("/next-favorite-question")
  ResponseEntity<AdminQuestionResponse> getNextFavoriteQuestion(
      @RequestParam("currentQuestionId")
          @NotBlank(message = "The current question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The current question ID must contain numeric characters only")
          String currentQuestionId,
      @RequestParam("getCurrent") boolean getCurrent);

  @PostMapping("/check-answer/{questionId}")
  ResponseEntity<CheckAnswerResponse> checkAnswer(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody CheckAnswerPayload payload);

  @GetMapping("/get-for-user/group/{groupId}")
  ResponseEntity<List<UserAdminQuestionResponse>> getQuestionsByGroupForUser(
      @PathVariable("groupId")
          @NotBlank(message = "The group ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The group ID must contain numeric characters only")
          String groupId);

  @PutMapping("/set-favorite/{questionId}")
  ResponseEntity<DynamicResponse> setFavorite(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody SetFavoritePayload payload);

  @GetMapping("/get-for-user/favorite")
  ResponseEntity<List<UserAdminQuestionResponse>> getFavoriteQuestionsForUser();

  @PostMapping("/user-questions")
  ResponseEntity<FullUserQuestionResponse> addUserQuestion(
      @Valid @RequestBody BaseQuestionPayload payload);

  @PutMapping("/user-questions/{questionId}")
  ResponseEntity<FullUserQuestionResponse> updateUserQuestion(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId,
      @Valid @RequestBody BaseQuestionPayload payload);

  @DeleteMapping("/user-questions/{questionId}")
  ResponseEntity<DynamicResponse> deleteUserQuestion(
      @PathVariable("questionId")
          @NotBlank(message = "The question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The question ID must contain numeric characters only")
          String questionId);

  @GetMapping("/user-questions")
  ResponseEntity<List<BriefUserQuestionResponse>> getUserQuestionsForUser();

  @GetMapping("/user-questions/next-user-question")
  ResponseEntity<FullUserQuestionResponse> getNextUserQuestion(
      @RequestParam("currentQuestionId")
          @NotBlank(message = "The current question ID is required")
          @Pattern(
              regexp = RegexConstants.COMMON_ID_PATTERN,
              message = "The current question ID must contain numeric characters only")
          String currentQuestionId,
      @RequestParam("getCurrent") boolean getCurrent);
}
