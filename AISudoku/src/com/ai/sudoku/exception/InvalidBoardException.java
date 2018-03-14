package com.ai.sudoku.exception;

public class InvalidBoardException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBoardException() {}

	public InvalidBoardException(String message) {
		super(message);
	}

	public InvalidBoardException(Throwable exception) {
		super(exception);
	}
}
