package com.rocksolid.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<String> handleEmailAlreadyExistsException(final EmailAlreadyExistsException ex) {
    return ResponseEntity.status(409).body("Tento e-mail už je zaregistrovaný.");
  }

}
