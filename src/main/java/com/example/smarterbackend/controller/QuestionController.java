package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.QuestionAPI;
import com.example.smarterbackend.framework.dto.DynamicResponse;
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
  public ResponseEntity<List<AdminQuestionResponse>> getQuestionsByGroup(String groupId) {
    return ResponseEntity.ok(questionService.getQuestionsByGroup(groupId));
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> getQuestionById(String questionId) {
    return ResponseEntity.ok(questionService.getQuestionById(questionId));
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> addQuestion(AdminQuestionPayload payload) {
    return new ResponseEntity<>(questionService.addQuestion(payload), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> updateQuestion(
      String questionId, AdminQuestionPayload payload) {
    return ResponseEntity.ok(questionService.updateQuestion(questionId, payload));
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> getRandomQuestion() {
    return ResponseEntity.ok(questionService.getRandomQuestion());
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> getNextQuestionInGroup(
      String currentQuestionId, boolean getCurrent) {
    return ResponseEntity.ok(questionService.getNextQuestionInGroup(currentQuestionId, getCurrent));
  }

  @Override
  public ResponseEntity<AdminQuestionResponse> getNextFavoriteQuestion(
      String currentQuestionId, boolean getCurrent) {
    return ResponseEntity.ok(
        questionService.getNextFavoriteQuestionOfCurrentUser(currentQuestionId, getCurrent));
  }

  @Override
  public ResponseEntity<CheckAnswerResponse> checkAnswer(
      String questionId, CheckAnswerPayload payload) {
    return ResponseEntity.ok(questionService.checkAnswer(questionId, payload));
  }

  @Override
  public ResponseEntity<List<UserAdminQuestionResponse>> getQuestionsByGroupForUser(
      String groupId) {
    return ResponseEntity.ok(questionService.getQuestionsByGroupForUser(groupId));
  }

  @Override
  public ResponseEntity<DynamicResponse> setFavorite(
      String questionId, SetFavoritePayload payload) {
    return ResponseEntity.ok(questionService.setFavorite(questionId, payload));
  }

  @Override
  public ResponseEntity<List<UserAdminQuestionResponse>> getFavoriteQuestionsForUser() {
    return ResponseEntity.ok(questionService.getFavoriteQuestionsForUser());
  }

  @Override
  public ResponseEntity<UserQuestionResponse> addUserQuestion(BaseQuestionPayload payload) {
    return new ResponseEntity<>(questionService.addUserQuestion(payload), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<UserQuestionResponse> updateUserQuestion(
      String questionId, BaseQuestionPayload payload) {
    return ResponseEntity.ok(questionService.updateUserQuestion(questionId, payload));
  }

  @Override
  public ResponseEntity<DynamicResponse> deleteUserQuestion(String questionId) {
    return ResponseEntity.ok(questionService.deleteUserQuestion(questionId));
  }

  @Override
  public ResponseEntity<List<UserQuestionResponse>> getUserQuestionsForUser() {
    return ResponseEntity.ok(questionService.getUserQuestionsForUser());
  }

  @Override
  public ResponseEntity<UserQuestionResponse> getNextUserQuestion(
      String currentQuestionId, boolean getCurrent) {
    return ResponseEntity.ok(questionService.getNextUserQuestion(currentQuestionId, getCurrent));
  }
}
