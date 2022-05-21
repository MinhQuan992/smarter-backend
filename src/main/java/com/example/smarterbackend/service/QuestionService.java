package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.InvalidRequestException;
import com.example.smarterbackend.exception.NoContentException;
import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.framework.common.data.Answer;
import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.question.*;
import com.example.smarterbackend.mapper.AdminQuestionMapper;
import com.example.smarterbackend.mapper.UserAdminQuestionMapper;
import com.example.smarterbackend.mapper.UserQuestionMapper;
import com.example.smarterbackend.model.*;
import com.example.smarterbackend.repository.*;
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
  private final UserQuestionRepository userQuestionRepository;
  private final QuestionGroupRepository questionGroupRepository;
  private final UserAdminQuestionRepository userAdminQuestionRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final UserAdminQuestionMapper userAdminQuestionMapper;
  private final AdminQuestionMapper adminQuestionMapper;
  private final UserQuestionMapper userQuestionMapper;

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
    AdminQuestion adminQuestion = findAdminQuestionById(questionId);
    return adminQuestionMapper.adminQuestionToAdminQuestionDTO(adminQuestion, null);
  }

  private AdminQuestion findAdminQuestionById(String questionId) {
    return adminQuestionRepository
        .findById(Long.parseLong(questionId))
        .orElseThrow(
            () -> {
              log.error("Could not find question, ID {}", questionId);
              return new NotFoundException("Question not found");
            });
  }

  private UserQuestion findUserQuestionById(String questionId) {
    return userQuestionRepository
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
    AdminQuestion adminQuestion = findAdminQuestionById(questionId);
    QuestionGroup newQuestionGroup = findQuestionGroupById(payload.getGroupId());
    updateQuestionProperties(adminQuestion, payload);

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

  private <T extends BaseQuestion, B extends BaseQuestionPayload> void updateQuestionProperties(
      T question, B payload) {
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

  private <T extends BaseQuestion> int getNextQuestionIndex(T question, List<T> questions) {
    int questionIndexInList = questions.indexOf(question);
    return (questionIndexInList == questions.size() - 1) ? 0 : questionIndexInList + 1;
  }

  public CheckAnswerResponse checkAnswer(String questionId, CheckAnswerPayload payload) {
    log.info("Checking answer for question, ID {}", questionId);
    User currentUser = userService.getCurrentUser();
    BaseQuestion question;
    try {
      question = findAdminQuestionById(questionId);
    } catch (NotFoundException ex) {
      question = findUserQuestionById(questionId);
      if (!currentUser.getUserQuestions().contains(question)) {
        throw new InvalidRequestException("You are not the author of this question");
      }
    }
    Answer chosenAnswer = Answer.fromCode(payload.getChosenAnswer());
    boolean isAnswerCorrect = question.getCorrectAnswer().equals(chosenAnswer);

    if (question instanceof AdminQuestion) {
      Optional<UserAdminQuestion> userQuestion =
          userAdminQuestionRepository.findByUserAndAdminQuestion(
              currentUser, (AdminQuestion) question);
      if (userQuestion.isEmpty()) {
        currentUser.addAnsweredQuestions(
            new UserAdminQuestion(currentUser, (AdminQuestion) question, isAnswerCorrect, false));
        userRepository.save(currentUser);
      } else {
        userQuestion.get().setCorrect(isAnswerCorrect);
        userAdminQuestionRepository.save(userQuestion.get());
      }
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
    AdminQuestion adminQuestion = findAdminQuestionById(questionId);
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

  public UserQuestionResponse addUserQuestion(BaseQuestionPayload payload) {
    User currentUser = userService.getCurrentUser();
    log.info("Adding new user-question for user ID {}", currentUser.getId());

    Answer correctAnswer = Answer.fromCode(payload.getCorrectAnswer());
    validateCorrectAnswer(
        correctAnswer,
        payload.getAnswerA(),
        payload.getAnswerB(),
        payload.getAnswerC(),
        payload.getAnswerD());

    UserQuestion userQuestion =
        UserQuestion.builder()
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

    currentUser.addUserQuestions(userQuestion);

    return userQuestionMapper.userQuestionToUserQuestionDTO(
        userQuestionRepository.save(userQuestion));
  }

  public UserQuestionResponse updateUserQuestion(String questionId, BaseQuestionPayload payload) {
    User currentUser = userService.getCurrentUser();
    log.info("Updating user-question ID {}", questionId);
    UserQuestion userQuestion = findUserQuestionById(questionId);

    if (!currentUser.getUserQuestions().contains(userQuestion)) {
      throw new InvalidRequestException("You are not the author of this question");
    }

    updateQuestionProperties(userQuestion, payload);
    return userQuestionMapper.userQuestionToUserQuestionDTO(
        userQuestionRepository.save(userQuestion));
  }

  public DynamicResponse deleteUserQuestion(String questionId) {
    User currentUser = userService.getCurrentUser();
    log.info("Deleting user-question ID {}", questionId);
    UserQuestion userQuestion = findUserQuestionById(questionId);

    if (!currentUser.getUserQuestions().contains(userQuestion)) {
      throw new InvalidRequestException("You are not the author of this question");
    }

    currentUser.removeQuestion(userQuestion);
    userRepository.save(currentUser);

    DynamicResponse response = new DynamicResponse();
    Map<String, Boolean> properties = new HashMap<>();
    properties.put("isDeleted", true);
    response.setProperties(properties);
    return response;
  }

  public List<UserQuestionResponse> getUserQuestionsForUser() {
    User currentUser = userService.getCurrentUser();
    log.info("Getting all user-questions for user ID {}", currentUser.getId());
    if (currentUser.getUserQuestions().isEmpty()) {
      throw new NoContentException();
    }
    return userQuestionMapper.listUserQuestionToListUserQuestionDTO(currentUser.getUserQuestions());
  }

  public UserQuestionResponse getNextUserQuestion(String currentQuestionId, boolean getCurrent) {
    User currentUser = userService.getCurrentUser();
    UserQuestion userQuestion = findUserQuestionById(currentQuestionId);
    List<UserQuestion> userQuestions = currentUser.getUserQuestions();

    if (!userQuestions.contains(userQuestion)) {
      throw new InvalidRequestException("You are not the author of this question");
    }

    if (getCurrent) {
      log.info("Getting user-question in user-question list, question ID: {}", currentQuestionId);
      return userQuestionMapper.userQuestionToUserQuestionDTO(userQuestion);
    }

    log.info(
        "Getting next user-question in user-question list, current question ID: {}",
        currentQuestionId);
    int nextQuestionIndex = getNextQuestionIndex(userQuestion, userQuestions);
    return userQuestionMapper.userQuestionToUserQuestionDTO(userQuestions.get(nextQuestionIndex));
  }
}
