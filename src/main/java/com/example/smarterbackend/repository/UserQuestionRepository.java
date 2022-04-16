package com.example.smarterbackend.repository;

import com.example.smarterbackend.model.Question;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.model.UserQuestion;
import com.example.smarterbackend.model.compositekey.UserQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, UserQuestionId> {
  @Query("SELECT uq.question FROM UserQuestion uq WHERE uq.user = :user")
  List<Question> getAnsweredQuestions(@Param("user") User user);

  List<UserQuestion> findAllByIsFavoriteIsTrueAndUserEquals(User user);
}
