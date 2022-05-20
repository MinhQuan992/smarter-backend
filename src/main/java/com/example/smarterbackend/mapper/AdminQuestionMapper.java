package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.AdminQuestionResponse;
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
public class AdminQuestionMapper {
  public AdminQuestionResponse adminQuestionToAdminQuestionDTO(
      AdminQuestion adminQuestion, List<UserAdminQuestion> answeredQuestions) {
    AdminQuestionResponse adminQuestionResponse =
        AdminQuestionResponse.builder()
            .id(adminQuestion.getId())
            .content(adminQuestion.getContent())
            .answerA(adminQuestion.getAnswerA())
            .answerB(adminQuestion.getAnswerB())
            .answerC(adminQuestion.getAnswerC())
            .answerD(adminQuestion.getAnswerD())
            .correctAnswer(adminQuestion.getCorrectAnswer())
            .imageUrl(adminQuestion.getImageUrl())
            .information(adminQuestion.getInformation())
            .reference(adminQuestion.getReference())
            .groupId(adminQuestion.getGroup().getId())
            .build();

    if (answeredQuestions != null) {
      Map<String, Object> questionChecker =
          QuestionUtils.checkQuestion(adminQuestion, answeredQuestions);
      boolean isAnswered = (boolean) questionChecker.get("isAnswered");
      if (isAnswered) {
        int index = (int) questionChecker.get("index");
        adminQuestionResponse.setFavorite(answeredQuestions.get(index).isFavorite());
      } else {
        adminQuestionResponse.setFavorite(false);
      }
    }

    return adminQuestionResponse;
  }

  public List<AdminQuestionResponse> listAdminQuestionToListAdminQuestionDTO(
      List<AdminQuestion> adminQuestions, List<UserAdminQuestion> answeredQuestions) {
    return adminQuestions.stream()
        .map(question -> adminQuestionToAdminQuestionDTO(question, answeredQuestions))
        .collect(Collectors.toList());
  }
}
