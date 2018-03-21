package com.ai.sudoku.analysis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;

/**
 * Attempts to solve the input sudoku board using a backtracking search tree
 * 
 * @author Joe Bender
 * @date Mar 20, 2018
 */
public class BacktrackingSearch {

	private SudokuBoard board;
	private int nodesExplored = 0;

	public BacktrackingSearch(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Square> squares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		searchTreeForSolution(0, squares);
		System.out.println("Backtracking Search Successfully Converged!");
		System.out.println(String.format("Nodes Explored: %s", nodesExplored));
		board.print();
	}

	private boolean searchTreeForSolution(int squareIndex, List<@NonNull Square> squares) {
		if (squareIndex < squares.size()) {
			squares.subList(squareIndex, squares.size()).forEach(Square::resetGuess);
			Square square = squares.get(squareIndex);
			Optional<Integer> value = square.getValue();
			for (int guess = 1; guess <= board.getDomain(); guess++) {
				if (value.isPresent() && value.get() != guess) continue; //Don't make branch when square's domain is known
				nodesExplored++;
				square.setBestGuess(guess);
				board.getConstraints().forEach(c -> c.setSatisfied(true));			
				if (square.getConstraints().stream().noneMatch(Constraint::isViolated)) {
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
