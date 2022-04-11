package com.example.smarterbackend.framework.common.data;

import com.example.smarterbackend.exception.InvalidRequestException;

public enum Answer {
  ANSWER_A, ANSWER_B, ANSWER_C, ANSWER_D;

  public static Answer fromCode(String code) {
    switch (code) {
      case "A", "a" -> {
        return ANSWER_A;
      }
      case "B", "b" -> {
        return ANSWER_B;
      }
      case "C", "c" -> {
        return ANSWER_C;
      }
      case "D", "d" -> {
        return ANSWER_D;
      }
      default -> throw new InvalidRequestException(String.format("No matching constant for [%s]", code));
    }
  }
}
