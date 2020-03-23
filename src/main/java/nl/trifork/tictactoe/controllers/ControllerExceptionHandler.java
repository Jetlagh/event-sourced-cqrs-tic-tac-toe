package nl.trifork.tictactoe.controllers;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import nl.trifork.tictactoe.domain.exceptions.GameAlreadyFinishedException;
import nl.trifork.tictactoe.domain.exceptions.InvalidCellLocationException;
import nl.trifork.tictactoe.domain.exceptions.InvalidPlayerForTurnException;
import nl.trifork.tictactoe.domain.exceptions.TileIsAlreadyFilledException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ GameAlreadyFinishedException.class, InvalidCellLocationException.class,
			InvalidPlayerForTurnException.class, TileIsAlreadyFilledException.class })
	protected ResponseEntity<Object> handleCustomException(final RuntimeException ex, final WebRequest request) {
		final String bodyOfResponse = ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler({ AggregateNotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
		final String bodyOfResponse = ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

}
