package com.ai.sudoku.analysis;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Lists;

/**
 * Attempts to solve the input sudoku board using arc consistancy
 * 
 * @author Joe Bender
 * @date Mar 20, 2018
 */
public class ArcConsistancy {

	private SudokuBoard board;
	private int constraintsProcessed = 0;

	public ArcConsistancy(SudokuBoard board) {
		this.board = board;
	}

	public void run() throws InvalidBoardException {
		maintainArcConsistancy(false);
		while (!board.isSolved() && board.containsUnsatisfiedConstraints()) {
			for (Collection<@NonNull Square> row : board.getRows()) {
				for (Square square : row) {
					for (int guessVal : Lists.newArrayList(square.getPossibilities())) {
						board.resetGuesses();
						guessValue(square, guessVal);
					}
				}
			}
		}
		System.out.println("Arc Consistancy Successfully Converged!");
		System.out.println(String.format("Constraints Processed: %s", constraintsProcessed));
		board.print();
	}

	private void guessValue(Square square, int guessVal) throws InvalidBoardException {
		square.setBestGuess(guessVal);
		try {
			maintainArcConsistancy(true);			
		} catch (InvalidBoardException e) {
			square.removePossibility(guessVal, false);
		}
		square.resetGuess();
	}

	private void maintainArcConsistancy(boolean isGuess) throws InvalidBoardException {
		while (board.containsUnsatisfiedConstraints()) {
			for (Constraint constraint : board.getConstraints()) {
				if (!constraint.isSatisfied()) {
					constraint.invoke(isGuess);
					constraint.setSatisfied(true);
					constraintsProcessed++;
				}
			}
		}
	}
}
