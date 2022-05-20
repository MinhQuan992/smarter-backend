package com.example.smarterbackend.repository;

import com.example.smarterbackend.model.AdminQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminQuestionRepository extends JpaRepository<AdminQuestion, Long> {
}
