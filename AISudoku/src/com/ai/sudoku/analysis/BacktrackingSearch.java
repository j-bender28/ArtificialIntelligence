package com.ai.sudoku.analysis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;

public class BacktrackingSearch {

	private SudokuBoard board;

	public BacktrackingSearch(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Square> squares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		searchTreeForSolution(0, squares);
		board.print();
	}

	private boolean searchTreeForSolution(int squareIndex, List<@NonNull Square> squares) {
		if (squareIndex < squares.size()) {
			squares.subList(squareIndex, squares.size()).forEach(Square::resetGuess);
			Square square = squares.get(squareIndex);
			Optional<Integer> value = square.getValue();
			for (int guess = 1; guess <= board.getDomain(); guess++) {
				if (value.isPresent() && value.get() != guess) continue; //Don't make branch when square's domain is known
				System.out.println(String.format("Guessing: %s, %s", guess, square));
				square.setBestGuess(guess);
				board.getConstraints().forEach(c -> c.setSatisfied(true));				
				if (square.getConstraints().stream().noneMatch(Constraint::isViolated)) {
					board.print();
					if (searchTreeForSolution(squareIndex + 1, squares)) {
						return true;
					}
				}
				square.resetGuess();
			}
		}
		return board.isSolved();
	}
}
