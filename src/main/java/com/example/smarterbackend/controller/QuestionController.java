package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.QuestionAPI;
import com.example.smarterbackend.framework.dto.question.*;
import com.example.smarterbackend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionAPI {
  private final QuestionService questionService;

  @Override
  public ResponseEntity<List<QuestionResponse>> getQuestionsByGroup(String groupId) {
    return ResponseEntity.ok(questionService.getQuestionsByGroup(groupId));
  }

  @Override
  public ResponseEntity<QuestionResponse> getQuestionById(String questionId) {
    return ResponseEntity.ok(questionService.getQuestionById(questionId));
  }

  @Override
  public ResponseEntity<QuestionResponse> addQuestion(QuestionPayload payload) {
    return new ResponseEntity<>(questionService.addQuestion(payload), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<QuestionResponse> updateQuestion(
      String questionId, QuestionPayload payload) {
    return ResponseEntity.ok(questionService.updateQuestion(questionId, payload));
  }

  @Override
  public ResponseEntity<CheckAnswerResponse> checkAnswer(
      String questionId, CheckAnswerPayload payload) {
    return ResponseEntity.ok(questionService.checkAnswer(questionId, payload));
  }
}
