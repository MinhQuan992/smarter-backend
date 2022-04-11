package com.example.smarterbackend.framework.common.data;

import com.example.smarterbackend.exception.InvalidRequestException;

public enum Gender {
  MALE,
  FEMALE;

  public static Gender fromGenderString(String genderString) {
    switch (genderString) {
      case "male" -> {
        return MALE;
      }
      case "female" -> {
        return FEMALE;
      }
      default -> throw new InvalidRequestException(String.format("No matching constant for [%s]", genderString));
    }
  }
}
