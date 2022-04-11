package com.example.smarterbackend.controller;

import com.example.smarterbackend.framework.api.QuestionGroupAPI;
import com.example.smarterbackend.framework.dto.questiongroup.AddQuestionGroupPayload;
import com.example.smarterbackend.framework.dto.questiongroup.QuestionGroupResponse;
import com.example.smarterbackend.service.QuestionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionGroupController implements QuestionGroupAPI {
  private final QuestionGroupService questionGroupService;

  @Override
  public ResponseEntity<List<QuestionGroupResponse>> getAllQuestionGroups() {
    return ResponseEntity.ok(questionGroupService.getAllQuestionGroups());
  }

  @Override
  public ResponseEntity<QuestionGroupResponse> addQuestionGroup(AddQuestionGroupPayload payload) {
    return new ResponseEntity<>(questionGroupService.addQuestionGroup(payload), HttpStatus.CREATED);
  }
}
