package com.example.smarterbackend.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Map<String, List<String>> body = new HashMap<>();
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    body.put("errors", errors);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ExceptionHandler(NoContentException.class)
  public ErrorMessage noContentException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({InvalidRequestException.class})
  public ErrorMessage badRequestException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorMessage notFoundException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({ResourceConflictException.class})
  public ErrorMessage resourceConflictException(Exception exception) {
    return new ErrorMessage(exception.getMessage());
  }
}
