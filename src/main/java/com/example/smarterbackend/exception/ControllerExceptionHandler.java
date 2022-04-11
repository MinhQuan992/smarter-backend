package com.example.smarterbackend.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
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
  public ResponseEntity<ErrorMessage> noContentException() {
    return new ResponseEntity<>(new ErrorMessage(), HttpStatus.NO_CONTENT);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({InvalidRequestException.class, OtpException.class})
  public ResponseEntity<ErrorMessage> badRequestException(Exception exception) {
    return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorMessage> forbiddenException(Exception exception) {
    if (exception.getMessage() == null) {
      return null;
    }
    return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> notFoundException(Exception exception) {
    return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({ResourceConflictException.class})
  public ResponseEntity<ErrorMessage> resourceConflictException(Exception exception) {
    return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.CONFLICT);
  }
}
