package com.example.smarterbackend.framework.api;

import com.example.smarterbackend.framework.dto.questiongroup.AddQuestionGroupPayload;
import com.example.smarterbackend.framework.dto.questiongroup.QuestionGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/question-groups")
@Validated
public interface QuestionGroupAPI {
  @GetMapping
  ResponseEntity<List<QuestionGroupResponse>> getAllQuestionGroups();

  @PostMapping
  ResponseEntity<QuestionGroupResponse> addQuestionGroup(
      @Valid @RequestBody AddQuestionGroupPayload payload);
}
