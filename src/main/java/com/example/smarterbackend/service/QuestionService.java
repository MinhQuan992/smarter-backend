package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.InvalidRequestException;
import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.framework.common.data.Answer;
import com.example.smarterbackend.framework.dto.question.CheckAnswerPayload;
import com.example.smarterbackend.framework.dto.question.CheckAnswerResponse;
import com.example.smarterbackend.framework.dto.question.QuestionPayload;
import com.example.smarterbackend.framework.dto.question.QuestionResponse;
import com.example.smarterbackend.mapper.QuestionMapper;
import com.example.smarterbackend.model.Question;
import com.example.smarterbackend.model.QuestionGroup;
import com.example.smarterbackend.repository.QuestionGroupRepository;
import com.example.smarterbackend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;
  private final QuestionGroupRepository questionGroupRepository;

  public List<QuestionResponse> getQuestionsByGroup(String groupId) {
    log.info("Getting all questions of group {}", groupId);
    QuestionGroup questionGroup = findQuestionGroupById(groupId);
    return QuestionMapper.INSTANCE.listQuestionToListQuestionDTO(questionGroup.getQuestions());
  }

  private QuestionGroup findQuestionGroupById(String groupId) {
    return questionGroupRepository
        .findById(Long.parseLong(groupId))
        .orElseThrow(
            () -> {
              log.error("Could not find group {}", groupId);
              return new NotFoundException("Question group not found");
            });
  }

  public QuestionResponse getQuestionById(String questionId) {
    log.info("Getting one question, ID {}", questionId);
    Question question = findQuestionById(questionId);
    return QuestionMapper.INSTANCE.questionToQuestionDTO(question);
  }

  private Question findQuestionById(String questionId) {
    return questionRepository
        .findById(Long.parseLong(questionId))
        .orElseThrow(
            () -> {
              log.error("Could not find question, ID {}", questionId);
              return new NotFoundException("Question not found");
            });
  }

  public QuestionResponse addQuestion(QuestionPayload payload) {
    log.info("Adding a new question to group {}", payload.getGroupId());
    QuestionGroup questionGroup = findQuestionGroupById(payload.getGroupId());

    Answer correctAnswer = Answer.fromCode(payload.getCorrectAnswer());
    validateCorrectAnswer(
        correctAnswer,
        payload.getAnswerA(),
        payload.getAnswerB(),
        payload.getAnswerC(),
        payload.getAnswerD());

    Question question =
        Question.builder()
            .content(payload.getContent())
            .answerA(payload.getAnswerA())
            .answerB(payload.getAnswerB())
            .answerC(payload.getAnswerC())
            .answerD(payload.getAnswerD())
            .correctAnswer(correctAnswer)
            .imageUrl(payload.getImageUrl())
            .information(payload.getInformation())
            .reference(payload.getReference())
            .build();
    questionGroup.addQuestion(question);

    return QuestionMapper.INSTANCE.questionToQuestionDTO(questionRepository.save(question));
  }

  private void validateCorrectAnswer(
      Answer correctAnswer, String answerA, String answerB, String answerC, String answerD) {
    if ((correctAnswer.equals(Answer.ANSWER_A) && answerA.isEmpty())
        || (correctAnswer.equals(Answer.ANSWER_B) && answerB.isEmpty())
        || (correctAnswer.equals(Answer.ANSWER_C) && answerC.isEmpty())
        || (correctAnswer.equals(Answer.ANSWER_D) && answerD.isEmpty())) {
      throw new InvalidRequestException("The correct answer is invalid");
    }
  }

  public QuestionResponse updateQuestion(String questionId, QuestionPayload payload) {
    log.info("Updating one question, ID {}", questionId);
    Question question = findQuestionById(questionId);
    QuestionGroup newQuestionGroup = findQuestionGroupById(payload.getGroupId());
    Answer correctAnswer = Answer.fromCode(payload.getCorrectAnswer());
    validateCorrectAnswer(
        correctAnswer,
        payload.getAnswerA(),
        payload.getAnswerB(),
        payload.getAnswerC(),
        payload.getAnswerD());

    question.setContent(payload.getContent());
    question.setAnswerA(payload.getAnswerA());
    question.setAnswerB(payload.getAnswerB());
    question.setAnswerC(payload.getAnswerC());
    question.setAnswerD(payload.getAnswerD());
    question.setCorrectAnswer(correctAnswer);
    question.setImageUrl(payload.getImageUrl());
    question.setInformation(payload.getInformation());
    question.setReference(payload.getReference());

    QuestionGroup oldQuestionGroup = question.getGroup();
    if (!newQuestionGroup.equals(oldQuestionGroup)) {
      String oldGroupName = oldQuestionGroup.getName();
      String newGroupName = newQuestionGroup.getName();

      log.info(
          "Old group {} has {} questions", oldGroupName, oldQuestionGroup.getQuestions().size());
      log.info(
          "New group {} has {} questions", newGroupName, newQuestionGroup.getQuestions().size());
      log.info("Changing the group of the question from {} to {}", oldGroupName, newGroupName);

      oldQuestionGroup.removeQuestion(question);
      newQuestionGroup.addQuestion(question);

      log.info(
          "After changing, group {} has {} questions, group {} has {} questions",
          oldGroupName,
          oldQuestionGroup.getQuestions().size(),
          newGroupName,
          newQuestionGroup.getQuestions().size());
    }

    return QuestionMapper.INSTANCE.questionToQuestionDTO(questionRepository.save(question));
  }

  public CheckAnswerResponse checkAnswer(String questionId, CheckAnswerPayload payload) {
    log.info("Checking answer for question, ID {}", questionId);
    Question question = findQuestionById(questionId);
    Answer chosenAnswer = Answer.fromCode(payload.getChosenAnswer());

    CheckAnswerResponse response = new CheckAnswerResponse();
    response.setAnswerCorrect(question.getCorrectAnswer().equals(chosenAnswer));
    return response;
  }
}
