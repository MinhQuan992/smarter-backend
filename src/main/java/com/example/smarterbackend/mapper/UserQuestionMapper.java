package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.UserQuestionResponse;
import com.example.smarterbackend.model.UserQuestion;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserQuestionMapper {
  public UserQuestionResponse userQuestionToUserQuestionDTO(UserQuestion userQuestion) {
    return UserQuestionResponse.builder()
        .id(userQuestion.getId())
        .content(userQuestion.getContent())
        .answerA(userQuestion.getAnswerA())
        .answerB(userQuestion.getAnswerB())
        .answerC(userQuestion.getAnswerC())
        .answerD(userQuestion.getAnswerD())
        .correctAnswer(userQuestion.getCorrectAnswer())
        .imageUrl(userQuestion.getImageUrl())
        .information(userQuestion.getInformation())
        .reference(userQuestion.getReference())
        .authorId(userQuestion.getAuthor().getId())
        .shortContent(QuestionUtils.getShortContentFromQuestion(userQuestion))
        .build();
  }

  public List<UserQuestionResponse> listUserQuestionToListUserQuestionDTO(
      List<UserQuestion> userQuestions) {
    return userQuestions.stream()
        .map(this::userQuestionToUserQuestionDTO)
        .collect(Collectors.toList());
  }
}
