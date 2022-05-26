package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.BriefUserQuestionResponse;
import com.example.smarterbackend.model.UserQuestion;
import com.example.smarterbackend.util.QuestionUtils;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class BriefUserQuestionMapper {
  public BriefUserQuestionResponse userQuestionToBriefUserQuestionDTO(UserQuestion userQuestion) {
    return BriefUserQuestionResponse.builder()
        .questionId(userQuestion.getId())
        .imageUrl(userQuestion.getImageUrl())
        .shortContent(QuestionUtils.getShortContentFromQuestion(userQuestion))
        .build();
  }

  public List<BriefUserQuestionResponse> listUserQuestionToListBriefUserQuestionDTO(
      List<UserQuestion> userQuestions) {
    return userQuestions.stream()
        .map(this::userQuestionToBriefUserQuestionDTO)
        .collect(Collectors.toList());
  }
}
