package org.fairytale.moviesdetailsservice.application.controller;

import org.fairytale.moviesdetailsservice.application.dto.ApiErrorDto;
import org.fairytale.moviesdetailsservice.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles the {@link ResourceNotFoundException} exception.
   *
   * @param ex the exception to handle
   * @return the response entity with the error
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public Mono<ApiErrorDto> handleResourceNotFoundException(final ResourceNotFoundException ex) {
    return Mono.just(new ApiErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
  }
}
