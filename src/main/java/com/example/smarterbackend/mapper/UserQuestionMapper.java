package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.UserQuestionResponse;
import com.example.smarterbackend.model.Question;
import com.example.smarterbackend.model.UserQuestion;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserQuestionMapper {
  private static final int DEFAULT_END_INDEX_OF_CONTENT = 15;

  private String getShortContentFromQuestion(Question question) {
    int endIndexOfContent = Math.min(question.getContent().length(), DEFAULT_END_INDEX_OF_CONTENT);
    return question.getContent().substring(0, endIndexOfContent) + "...";
  }

  public UserQuestionResponse questionToUserQuestionDTO(
      Question question, List<UserQuestion> answeredQuestions) {
    Map<String, Object> questionChecker = QuestionUtils.checkQuestion(question, answeredQuestions);
    boolean isAnswered = (boolean) questionChecker.get("isAnswered");

    if (isAnswered) {
      int index = (int) questionChecker.get("index");
      return UserQuestionResponse.builder()
          .questionId(question.getId())
          .shortContent(getShortContentFromQuestion(question))
          .isAnswered(true)
          .isAnswerCorrect(answeredQuestions.get(index).isCorrect())
          .isFavorite(answeredQuestions.get(index).isFavorite())
          .build();
    }

    return UserQuestionResponse.builder()
        .questionId(question.getId())
        .shortContent(getShortContentFromQuestion(question))
        .isAnswered(false)
        .isAnswerCorrect(false)
        .isFavorite(false)
        .build();
  }

  public List<UserQuestionResponse> listQuestionToListUserQuestionDTO(
      List<Question> questions, List<UserQuestion> answeredQuestions) {
    return questions.stream()
        .map(question -> questionToUserQuestionDTO(question, answeredQuestions))
        .collect(Collectors.toList());
  }

  public UserQuestionResponse userQuestionToUserQuestionDTO(UserQuestion userQuestion) {
    return UserQuestionResponse.builder()
        .questionId(userQuestion.getQuestion().getId())
        .shortContent(getShortContentFromQuestion(userQuestion.getQuestion()))
        .isAnswered(true)
        .isAnswerCorrect(userQuestion.isCorrect())
        .isFavorite(userQuestion.isFavorite())
        .build();
  }

  public List<UserQuestionResponse> listUserQuestionToListUserQuestionDTO(
      List<UserQuestion> userQuestions) {
    return userQuestions.stream()
        .map(this::userQuestionToUserQuestionDTO)
        .collect(Collectors.toList());
  }
}
