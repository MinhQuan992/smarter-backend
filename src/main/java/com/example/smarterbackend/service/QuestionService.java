package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.InvalidRequestException;
import com.example.smarterbackend.exception.NoContentException;
import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.framework.common.data.Answer;
import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.question.*;
import com.example.smarterbackend.mapper.AdminQuestionMapper;
import com.example.smarterbackend.mapper.UserAdminQuestionMapper;
import com.example.smarterbackend.model.AdminQuestion;
import com.example.smarterbackend.model.QuestionGroup;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.model.UserAdminQuestion;
import com.example.smarterbackend.repository.QuestionGroupRepository;
import com.example.smarterbackend.repository.AdminQuestionRepository;
import com.example.smarterbackend.repository.UserAdminQuestionRepository;
import com.example.smarterbackend.repository.UserRepository;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
  private final AdminQuestionRepository adminQuestionRepository;
  private final QuestionGroupRepository questionGroupRepository;
  private final UserAdminQuestionRepository userAdminQuestionRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final UserAdminQuestionMapper userAdminQuestionMapper;
  private final AdminQuestionMapper adminQuestionMapper;

  public List<AdminQuestionResponse> getQuestionsByGroup(String groupId) {
    log.info("Getting all questions of group {}", groupId);
    QuestionGroup questionGroup = findQuestionGroupById(groupId);
    return adminQuestionMapper.listAdminQuestionToListAdminQuestionDTO(
        questionGroup.getAdminQuestions(), null);
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

  public AdminQuestionResponse getQuestionById(String questionId) {
    log.info("Getting one question, ID {}", questionId);
    AdminQuestion adminQuestion = findQuestionById(questionId);
    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(adminQuestion, null);
  }

  private AdminQuestion findQuestionById(String questionId) {
    return adminQuestionRepository
        .findById(Long.parseLong(questionId))
        .orElseThrow(
            () -> {
              log.error("Could not find question, ID {}", questionId);
              return new NotFoundException("Question not found");
            });
  }

  public AdminQuestionResponse addQuestion(AdminQuestionPayload payload) {
    log.info("Adding a new question to group {}", payload.getGroupId());
    QuestionGroup questionGroup = findQuestionGroupById(payload.getGroupId());

    Answer correctAnswer = Answer.fromCode(payload.getCorrectAnswer());
    validateCorrectAnswer(
        correctAnswer,
        payload.getAnswerA(),
        payload.getAnswerB(),
        payload.getAnswerC(),
        payload.getAnswerD());

    AdminQuestion adminQuestion =
        AdminQuestion.builder()
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
    questionGroup.addQuestion(adminQuestion);

    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
        adminQuestionRepository.save(adminQuestion), null);
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

  public AdminQuestionResponse updateQuestion(String questionId, AdminQuestionPayload payload) {
    log.info("Updating one question, ID {}", questionId);
    AdminQuestion adminQuestion = findQuestionById(questionId);
    QuestionGroup newQuestionGroup = findQuestionGroupById(payload.getGroupId());
    Answer correctAnswer = Answer.fromCode(payload.getCorrectAnswer());
    validateCorrectAnswer(
        correctAnswer,
        payload.getAnswerA(),
        payload.getAnswerB(),
        payload.getAnswerC(),
        payload.getAnswerD());

    adminQuestion.setContent(payload.getContent());
    adminQuestion.setAnswerA(payload.getAnswerA());
    adminQuestion.setAnswerB(payload.getAnswerB());
    adminQuestion.setAnswerC(payload.getAnswerC());
    adminQuestion.setAnswerD(payload.getAnswerD());
    adminQuestion.setCorrectAnswer(correctAnswer);
    adminQuestion.setImageUrl(payload.getImageUrl());
    adminQuestion.setInformation(payload.getInformation());
    adminQuestion.setReference(payload.getReference());

    QuestionGroup oldQuestionGroup = adminQuestion.getGroup();
    if (!newQuestionGroup.equals(oldQuestionGroup)) {
      String oldGroupName = oldQuestionGroup.getName();
      String newGroupName = newQuestionGroup.getName();

      log.info(
          "Old group {} has {} questions",
          oldGroupName,
          oldQuestionGroup.getAdminQuestions().size());
      log.info(
          "New group {} has {} questions",
          newGroupName,
          newQuestionGroup.getAdminQuestions().size());
      log.info("Changing the group of the question from {} to {}", oldGroupName, newGroupName);

      oldQuestionGroup.removeQuestion(adminQuestion);
      newQuestionGroup.addQuestion(adminQuestion);

      log.info(
          "After changing, group {} has {} questions, group {} has {} questions",
          oldGroupName,
          oldQuestionGroup.getAdminQuestions().size(),
          newGroupName,
          newQuestionGroup.getAdminQuestions().size());
    }

    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
        adminQuestionRepository.save(adminQuestion), null);
  }

  public AdminQuestionResponse getRandomQuestion() {
    log.info("Getting one random question");

    // Step 1: Get all questions from the database
    List<AdminQuestion> adminQuestions = adminQuestionRepository.findAll();

    // Step 2: Get all answered questions of the current user
    User currentUser = userService.getCurrentUser();
    List<AdminQuestion> answeredQuestions =
        userAdminQuestionRepository.getAnsweredQuestions(currentUser);

    // Step 3: Get all non-answered questions of the current user if there are some questions
    // they've not answered
    if (answeredQuestions.size() < adminQuestions.size()) {
      adminQuestions.removeAll(answeredQuestions);
    }

    // Step 4: Get a random question from the list
    Random random = new Random();
    int randomIndex = random.nextInt(adminQuestions.size());
    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
        adminQuestions.get(randomIndex), currentUser.getAnsweredQuestions());
  }

  public AdminQuestionResponse getNextQuestionInGroup(
      String currentQuestionId, boolean getCurrent) {
    AdminQuestion adminQuestion =
        adminQuestionRepository
            .findById(Long.parseLong(currentQuestionId))
            .orElseThrow(() -> new NotFoundException("Question not found"));
    User currentUser = userService.getCurrentUser();

    if (getCurrent) {
      log.info("Getting question in group, question ID: {}", currentQuestionId);
      return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
          adminQuestion, currentUser.getAnsweredQuestions());
    }

    log.info("Getting next question in group, current question ID: {}", currentQuestionId);
    List<AdminQuestion> adminQuestions = adminQuestion.getGroup().getAdminQuestions();
    int nextQuestionIndex = getNextQuestionIndex(adminQuestion, adminQuestions);
    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
        adminQuestions.get(nextQuestionIndex), currentUser.getAnsweredQuestions());
  }

  public AdminQuestionResponse getNextFavoriteQuestionOfCurrentUser(
      String currentQuestionId, boolean getCurrent) {
    User currentUser = userService.getCurrentUser();
    List<AdminQuestion> favoriteQuestions =
        userAdminQuestionRepository.getFavoriteQuestions(currentUser);
    if (favoriteQuestions.isEmpty()) {
      throw new InvalidRequestException("This user hasn't had any favorite questions");
    }

    AdminQuestion adminQuestion =
        adminQuestionRepository
            .findById(Long.parseLong(currentQuestionId))
            .orElseThrow(() -> new NotFoundException("Question not found"));

    if (!favoriteQuestions.contains(adminQuestion)) {
      return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
          favoriteQuestions.get(0), currentUser.getAnsweredQuestions());
    }

    if (getCurrent) {
      log.info("Getting question in favorite question list, question ID: {}", currentQuestionId);
      return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
          adminQuestion, currentUser.getAnsweredQuestions());
    }

    log.info(
        "Getting next question in favorite question list, current question ID: {}",
        currentQuestionId);
    int nextQuestionIndex = getNextQuestionIndex(adminQuestion, favoriteQuestions);
    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(
        favoriteQuestions.get(nextQuestionIndex), currentUser.getAnsweredQuestions());
  }

  private int getNextQuestionIndex(
      AdminQuestion adminQuestion, List<AdminQuestion> adminQuestions) {
    int questionIndexInList = adminQuestions.indexOf(adminQuestion);
    return (questionIndexInList == adminQuestions.size() - 1) ? 0 : questionIndexInList + 1;
  }

  public CheckAnswerResponse checkAnswer(String questionId, CheckAnswerPayload payload) {
    // TODO: make it generic
    log.info("Checking answer for question, ID {}", questionId);
    AdminQuestion adminQuestion = findQuestionById(questionId);
    Answer chosenAnswer = Answer.fromCode(payload.getChosenAnswer());
    boolean isAnswerCorrect = adminQuestion.getCorrectAnswer().equals(chosenAnswer);

    User currentUser = userService.getCurrentUser();
    Optional<UserAdminQuestion> userQuestion =
        userAdminQuestionRepository.findByUserAndAdminQuestion(currentUser, adminQuestion);
    if (userQuestion.isEmpty()) {
      currentUser.addAnsweredQuestions(
          new UserAdminQuestion(currentUser, adminQuestion, isAnswerCorrect, false));
      userRepository.save(currentUser);
    } else {
      userQuestion.get().setCorrect(isAnswerCorrect);
      userAdminQuestionRepository.save(userQuestion.get());
    }

    CheckAnswerResponse response = new CheckAnswerResponse();
    response.setAnswerCorrect(isAnswerCorrect);
    return response;
  }

  public List<UserAdminQuestionResponse> getQuestionsByGroupForUser(String groupId) {
    log.info("Getting all questions of group {} for current user", groupId);
    QuestionGroup questionGroup = findQuestionGroupById(groupId);
    List<AdminQuestion> adminQuestions = questionGroup.getAdminQuestions();

    if (adminQuestions.isEmpty()) {
      throw new NoContentException();
    }

    User currentUser = userService.getCurrentUser();
    List<UserAdminQuestion> answeredQuestions = currentUser.getAnsweredQuestions();

    return userAdminQuestionMapper.listAdminQuestionToListUserAdminQuestionDTO(
        adminQuestions, answeredQuestions);
  }

  public DynamicResponse setFavorite(String questionId, SetFavoritePayload payload) {
    log.info("Changing the favorite status of question {}", questionId);
    AdminQuestion adminQuestion = findQuestionById(questionId);
    User currentUser = userService.getCurrentUser();
    List<UserAdminQuestion> answeredQuestions = currentUser.getAnsweredQuestions();

    Map<String, Object> questionChecker =
        QuestionUtils.checkQuestion(adminQuestion, answeredQuestions);
    boolean isAnswered = (boolean) questionChecker.get("isAnswered");

    if (!isAnswered) {
      throw new InvalidRequestException("This question hasn't been answered yet");
    }

    int index = (int) questionChecker.get("index");
    answeredQuestions.get(index).setFavorite(payload.isFavorite());
    userRepository.save(currentUser);

    DynamicResponse response = new DynamicResponse();
    Map<String, Boolean> properties = new HashMap<>();
    properties.put("isFavorite", payload.isFavorite());
    response.setProperties(properties);
    return response;
  }

  public List<UserAdminQuestionResponse> getFavoriteQuestionsForUser() {
    log.info("Getting all favorite questions for current user");
    User currentUser = userService.getCurrentUser();
    List<UserAdminQuestion> favoriteQuestions =
        userAdminQuestionRepository.findAllByIsFavoriteIsTrueAndUserEquals(currentUser);
    if (favoriteQuestions.isEmpty()) {
      throw new NoContentException();
    }
    return userAdminQuestionMapper.listUserAdminQuestionToListUserAdminQuestionDTO(
        favoriteQuestions);
  }
}
