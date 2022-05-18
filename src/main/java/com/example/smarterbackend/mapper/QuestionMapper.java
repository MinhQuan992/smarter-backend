package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.QuestionResponse;
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
public class QuestionMapper {
  public QuestionResponse questionToQuestionDTO(
      Question question, List<UserQuestion> answeredQuestions) {
    QuestionResponse questionResponse =
        QuestionResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .answerA(question.getAnswerA())
            .answerB(question.getAnswerB())
            .answerC(question.getAnswerC())
            .answerD(question.getAnswerD())
            .correctAnswer(question.getCorrectAnswer())
            .imageUrl(question.getImageUrl())
            .information(question.getInformation())
            .reference(question.getReference())
            .groupId(question.getGroup().getId())
            .build();

    if (answeredQuestions != null) {
      Map<String, Object> questionChecker =
          QuestionUtils.checkQuestion(question, answeredQuestions);
      boolean isAnswered = (boolean) questionChecker.get("isAnswered");
      if (isAnswered) {
        int index = (int) questionChecker.get("index");
        questionResponse.setFavorite(answeredQuestions.get(index).isFavorite());
      } else {
        questionResponse.setFavorite(false);
      }
    }

    return questionResponse;
  }

  public List<QuestionResponse> listQuestionToListQuestionDTO(
      List<Question> questions, List<UserQuestion> answeredQuestions) {
    return questions.stream()
        .map(question -> questionToQuestionDTO(question, answeredQuestions))
        .collect(Collectors.toList());
  }
}
