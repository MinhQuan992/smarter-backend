package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.InvalidRequestException;
import com.example.smarterbackend.exception.NoContentException;
import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.framework.common.data.Answer;
import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.question.*;
import com.example.smarterbackend.mapper.QuestionMapper;
import com.example.smarterbackend.mapper.UserQuestionMapper;
import com.example.smarterbackend.model.Question;
import com.example.smarterbackend.model.QuestionGroup;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.model.UserQuestion;
import com.example.smarterbackend.repository.QuestionGroupRepository;
import com.example.smarterbackend.repository.QuestionRepository;
import com.example.smarterbackend.repository.UserQuestionRepository;
import com.example.smarterbackend.repository.UserRepository;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepository questionRepository;
  private final QuestionGroupRepository questionGroupRepository;
  private final UserQuestionRepository userQuestionRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final UserQuestionMapper userQuestionMapper;

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

  public QuestionResponse getRandomQuestion() {
    log.info("Getting one random question");

    // Step 1: Get all questions from the database
    List<Question> questions = questionRepository.findAll();

    // Step 2: Get all answered questions of the current user
    User currentUser = userService.getCurrentUser();
    List<Question> answeredQuestions = userQuestionRepository.getAnsweredQuestions(currentUser);

    // Step 3: Get all non-answered questions of the current user if there are some questions
    // they've not answered
    if (answeredQuestions.size() < questions.size()) {
      questions.removeAll(answeredQuestions);
    }

    // Step 4: Get a random question from the list
    Random random = new Random();
    int randomIndex = random.nextInt(questions.size());
    return QuestionMapper.INSTANCE.questionToQuestionDTO(questions.get(randomIndex));
  }

  public QuestionResponse getNextQuestionInGroup(String currentQuestionId, boolean getCurrent) {
    Question question =
        questionRepository
            .findById(Long.parseLong(currentQuestionId))
            .orElseThrow(() -> new NotFoundException("Question not found"));

    if (getCurrent) {
      log.info("Getting question in group, question ID: {}", currentQuestionId);
      return QuestionMapper.INSTANCE.questionToQuestionDTO(question);
    }

    log.info("Getting next question in group, current question ID: {}", currentQuestionId);
    List<Question> questions = question.getGroup().getQuestions();
    int nextQuestionIndex = getNextQuestionIndex(question, questions);
    return QuestionMapper.INSTANCE.questionToQuestionDTO(questions.get(nextQuestionIndex));
  }

  public QuestionResponse getNextFavoriteQuestionOfCurrentUser(
      String currentQuestionId, boolean getCurrent) {
    User currentUser = userService.getCurrentUser();
    List<Question> favoriteQuestions = userQuestionRepository.getFavoriteQuestions(currentUser);
    if (favoriteQuestions.isEmpty()) {
      throw new InvalidRequestException("This user hasn't had any favorite questions");
    }

    Question question =
        questionRepository
            .findById(Long.parseLong(currentQuestionId))
            .orElseThrow(() -> new NotFoundException("Question not found"));

    if (!favoriteQuestions.contains(question)) {
      throw new InvalidRequestException(
          "This question is not in the user's favorite question list");
    }

    if (getCurrent) {
      log.info("Getting question in favorite question list, question ID: {}", currentQuestionId);
      return QuestionMapper.INSTANCE.questionToQuestionDTO(question);
    }

    log.info(
        "Getting next question in favorite question list, current question ID: {}",
        currentQuestionId);
    int nextQuestionIndex = getNextQuestionIndex(question, favoriteQuestions);
    return QuestionMapper.INSTANCE.questionToQuestionDTO(favoriteQuestions.get(nextQuestionIndex));
  }

  private int getNextQuestionIndex(Question question, List<Question> questions) {
    int questionIndexInList = questions.indexOf(question);
    return (questionIndexInList == questions.size() - 1) ? 0 : questionIndexInList + 1;
  }

  public CheckAnswerResponse checkAnswer(String questionId, CheckAnswerPayload payload) {
    log.info("Checking answer for question, ID {}", questionId);
    Question question = findQuestionById(questionId);
    Answer chosenAnswer = Answer.fromCode(payload.getChosenAnswer());
    boolean isAnswerCorrect = question.getCorrectAnswer().equals(chosenAnswer);

    User currentUser = userService.getCurrentUser();
    UserQuestion userQuestion = new UserQuestion(currentUser, question, isAnswerCorrect, false);

    currentUser.addAnsweredQuestions(userQuestion);
    userRepository.save(currentUser);

    CheckAnswerResponse response = new CheckAnswerResponse();
    response.setAnswerCorrect(isAnswerCorrect);
    return response;
  }

  public List<UserQuestionResponse> getQuestionsByGroupForUser(String groupId) {
    log.info("Getting all questions of group {} for current user", groupId);
    QuestionGroup questionGroup = findQuestionGroupById(groupId);
    List<Question> questions = questionGroup.getQuestions();

    User currentUser = userService.getCurrentUser();
    List<UserQuestion> answeredQuestions = currentUser.getAnsweredQuestions();

    return userQuestionMapper.listQuestionToListUserQuestionDTO(questions, answeredQuestions);
  }

  public DynamicResponse setFavorite(String questionId, SetFavoritePayload payload) {
    log.info("Changing the favorite status of question {}", questionId);
    Question question = findQuestionById(questionId);
    User currentUser = userService.getCurrentUser();
    List<UserQuestion> answeredQuestions = currentUser.getAnsweredQuestions();

    Map<String, Object> questionChecker = QuestionUtils.checkQuestion(question, answeredQuestions);
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

  public List<UserQuestionResponse> getFavoriteQuestionsForUser() {
    log.info("Getting all favorite questions for current user");
    User currentUser = userService.getCurrentUser();
    List<UserQuestion> favoriteQuestions =
        userQuestionRepository.findAllByIsFavoriteIsTrueAndUserEquals(currentUser);
    if (favoriteQuestions.isEmpty()) {
      throw new NoContentException();
    }
    return userQuestionMapper.listUserQuestionToListUserQuestionDTO(favoriteQuestions);
  }
}
