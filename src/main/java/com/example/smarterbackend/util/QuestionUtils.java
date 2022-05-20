package com.example.smarterbackend.util;

import com.example.smarterbackend.model.AdminQuestion;
import com.example.smarterbackend.model.BaseQuestion;
import com.example.smarterbackend.model.UserAdminQuestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionUtils {
  private static final int DEFAULT_LENGTH_OF_CONTENT = 30;

  public static <T extends BaseQuestion> String getShortContentFromQuestion(T question) {
    int lengthOfContent = Math.min(question.getContent().length(), DEFAULT_LENGTH_OF_CONTENT);
    return question.getContent().substring(0, lengthOfContent) + "...";
  }

  public static Map<String, Object> checkQuestion(
          AdminQuestion adminQuestion, List<UserAdminQuestion> answeredQuestions) {
    boolean isAnswered = false;
    int numberOfQuestions = answeredQuestions.size();
    int index = 0;
    while (index < numberOfQuestions && !isAnswered) {
      if (answeredQuestions.get(index).getAdminQuestion().equals(adminQuestion)) {
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
