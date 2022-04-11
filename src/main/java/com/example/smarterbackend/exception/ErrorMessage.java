package com.example.smarterbackend.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ErrorMessage {
  private UUID id = UUID.randomUUID();
  private String message;

  public ErrorMessage() {}

  public ErrorMessage(String message) {
    this.message = message;
  }
}
