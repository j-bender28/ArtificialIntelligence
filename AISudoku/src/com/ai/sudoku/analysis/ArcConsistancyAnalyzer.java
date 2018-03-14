package com.ai.sudoku.analysis;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Lists;

public class ArcConsistancyAnalyzer {

	private SudokuBoard board;
	private List<Constraint> constraints;

	public ArcConsistancyAnalyzer(SudokuBoard board) {
		this.board = board;
		this.constraints = board.getConstraints();
	}

	public void run() throws InvalidBoardException {
		maintainArcConsistancy(false);
		do {
			for (Collection<@NonNull Square> row : board.getRows()) {
				for (Square square : row) {
					for (int guessVal : Lists.newArrayList(square.getPossibilities())) {
						board.resetGuesses();
						guessValue(square, guessVal);
					}
				}
			}
		} while (!board.isSolved() && board.containsUnsatisfiedConstraints());
	}

	private void guessValue(Square square, int guessVal) throws InvalidBoardException {
		System.out.println(String.format("Guessing: %s, Row: %s, Col: %s", guessVal, square.getRow(),
				square.getCol()));
		square.setBestGuess(guessVal);
		try {
			maintainArcConsistancy(true);			
		} catch (InvalidBoardException e) {
			System.out.println(" >> Impossible Board State!");
			square.removePossibility(guessVal, false);
			board.print();
		}
		square.resetGuess();
	}

	private void maintainArcConsistancy(boolean isGuess) throws InvalidBoardException {
		while (board.containsUnsatisfiedConstraints()) {
			for (Constraint constraint : constraints) {
				if (!constraint.isSatisfied()) {
					if (constraint.invoke(isGuess)) {
						System.out.println(String.format("Invoked %s, %s", constraint, isGuess ? "GUESS": "ACTUAL"));
						board.print();
					}
					constraint.setSatisfied(true);
				}
			}
		}
	}
}
