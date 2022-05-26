package com.example.smarterbackend.repository;

import com.example.smarterbackend.model.AdminQuestion;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.model.UserAdminQuestion;
import com.example.smarterbackend.model.compositekey.UserAdminQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAdminQuestionRepository extends JpaRepository<UserAdminQuestion, UserAdminQuestionId> {
  @Query("SELECT uq.adminQuestion FROM UserAdminQuestion uq WHERE uq.user = :user")
  List<AdminQuestion> getAnsweredQuestions(@Param("user") User user);

  @Query("SELECT uq.adminQuestion FROM UserAdminQuestion uq WHERE uq.user = :user AND uq.isFavorite = true")
  List<AdminQuestion> getFavoriteQuestions(@Param("user") User user);

  List<UserAdminQuestion> findAllByIsFavoriteIsTrueAndUserEquals(User user);

  Optional<UserAdminQuestion> findByUserAndAdminQuestion(User user, AdminQuestion adminQuestion);
}
