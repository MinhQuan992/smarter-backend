package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.UserAdminQuestionResponse;
import com.example.smarterbackend.model.AdminQuestion;
import com.example.smarterbackend.model.UserAdminQuestion;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserAdminQuestionMapper {
  public UserAdminQuestionResponse adminQuestionToUserAdminQuestionDTO(
      AdminQuestion adminQuestion, List<UserAdminQuestion> answeredQuestions) {
    Map<String, Object> questionChecker =
        QuestionUtils.checkQuestion(adminQuestion, answeredQuestions);
    boolean isAnswered = (boolean) questionChecker.get("isAnswered");

    if (isAnswered) {
      int index = (int) questionChecker.get("index");
      return UserAdminQuestionResponse.builder()
          .questionId(adminQuestion.getId())
          .shortContent(QuestionUtils.getShortContentFromQuestion(adminQuestion))
          .imageUrl(adminQuestion.getImageUrl())
          .isAnswered(true)
          .isAnswerCorrect(answeredQuestions.get(index).isCorrect())
          .isFavorite(answeredQuestions.get(index).isFavorite())
          .build();
    }

    return UserAdminQuestionResponse.builder()
        .questionId(adminQuestion.getId())
        .shortContent(QuestionUtils.getShortContentFromQuestion(adminQuestion))
        .imageUrl(adminQuestion.getImageUrl())
        .isAnswered(false)
        .isAnswerCorrect(false)
        .isFavorite(false)
        .build();
  }

  public List<UserAdminQuestionResponse> listAdminQuestionToListUserAdminQuestionDTO(
      List<AdminQuestion> adminQuestions, List<UserAdminQuestion> answeredQuestions) {
    return adminQuestions.stream()
        .map(question -> adminQuestionToUserAdminQuestionDTO(question, answeredQuestions))
        .collect(Collectors.toList());
  }

  public UserAdminQuestionResponse userAdminQuestionToUserAdminQuestionDTO(
      UserAdminQuestion userAdminQuestion) {
    return UserAdminQuestionResponse.builder()
        .questionId(userAdminQuestion.getAdminQuestion().getId())
        .shortContent(
            QuestionUtils.getShortContentFromQuestion(userAdminQuestion.getAdminQuestion()))
        .imageUrl(userAdminQuestion.getAdminQuestion().getImageUrl())
        .isAnswered(true)
        .isAnswerCorrect(userAdminQuestion.isCorrect())
        .isFavorite(userAdminQuestion.isFavorite())
        .build();
  }

  public List<UserAdminQuestionResponse> listUserAdminQuestionToListUserAdminQuestionDTO(
      List<UserAdminQuestion> userAdminQuestions) {
    return userAdminQuestions.stream()
        .map(this::userAdminQuestionToUserAdminQuestionDTO)
        .collect(Collectors.toList());
  }
}
