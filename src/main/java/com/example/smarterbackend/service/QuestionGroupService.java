package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.NoContentException;
import com.example.smarterbackend.framework.dto.questiongroup.AddQuestionGroupPayload;
import com.example.smarterbackend.framework.dto.questiongroup.QuestionGroupResponse;
import com.example.smarterbackend.mapper.QuestionGroupMapper;
import com.example.smarterbackend.model.QuestionGroup;
import com.example.smarterbackend.repository.QuestionGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionGroupService {
  private final QuestionGroupRepository questionGroupRepository;

  public List<QuestionGroupResponse> getAllQuestionGroups() {
    log.info("Getting all question groups from the database");
    List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
    if (questionGroups.isEmpty()) {
      log.info("There are no question groups");
      throw new NoContentException();
    }
    return QuestionGroupMapper.INSTANCE.listQuestionGroupToListQuestionGroupDTO(questionGroups);
  }

  public QuestionGroupResponse addQuestionGroup(AddQuestionGroupPayload payload) {
    log.info("Adding new question group {} to the database", payload.getName());
    QuestionGroup questionGroup = QuestionGroup.builder().name(payload.getName()).build();
    return QuestionGroupMapper.INSTANCE.questionGroupToQuestionGroupDTO(
        questionGroupRepository.save(questionGroup));
  }
}
