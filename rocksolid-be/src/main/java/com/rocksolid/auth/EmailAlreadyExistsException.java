package com.rocksolid.auth;


public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(final String message) {
    super(message);
  }
}