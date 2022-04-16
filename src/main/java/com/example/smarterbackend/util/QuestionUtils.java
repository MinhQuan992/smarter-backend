package com.example.smarterbackend.util;

import com.example.smarterbackend.model.Question;
import com.example.smarterbackend.model.UserQuestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionUtils {
  public static Map<String, Object> checkQuestion(
      Question question, List<UserQuestion> answeredQuestions) {
    boolean isAnswered = false;
    int numberOfQuestions = answeredQuestions.size();
    int index = 0;
    while (index < numberOfQuestions && !isAnswered) {
      if (answeredQuestions.get(index).getQuestion().equals(question)) {
        isAnswered = true;
      } else {
        index++;
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("isAnswered", isAnswered);
    result.put("index", index);
    return result;
  }
}
