package com.example.smarterbackend.repository;

import com.example.smarterbackend.model.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
}
