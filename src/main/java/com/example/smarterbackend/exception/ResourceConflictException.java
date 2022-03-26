package com.example.smarterbackend.exception;

public class ResourceConflictException extends RuntimeException {
  public ResourceConflictException(String message) {
    super(message);
  }
}
