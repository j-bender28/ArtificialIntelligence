package com.ai.sudoku.analysis;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;

/**
 * Attempts to solve the input sudoku board using a backtracking search tree
 * 
 * @author Joe Bender
 * @date Mar 20, 2018
 */
public class GradientDescent {
	
	/**
	 * 3) Gradient descent - implement the gradient descent approach using the following rules. 
	 * The evaluation function will be the number of binary inequality constraints that are being 
	 * violated by the current guess at a solution. Pick a random starting point that is consistent 
	 * with the domain constraints of the input. Make a list of the variables that can be changed. 
	 * At each iteration of the process, pick the next variable in the list following the one used 
	 * in the previous step (use the first variable in the very first iteration). Once a variable is 
	 * chosen, compute the evaluation function for each possible choice in its domain, and select 
	 * the value that gives the best result. If more than one value gives the best result, randomly 
	 * choose one. If the computed evaluation value is better than that of the current guess, make 
	 * this step by changing the variable's value. If the evaluation value was zero, you can stop. 
	 * If the evaluation function value does not improve, pick the next variable in the list and 
	 * repeat until a better guess is found. If you go through the entire list once with no improvement, 
	 * you have converged to a local minimum that is not the global minimum. Do a random restart 
	 * and continue. Keep track of the total number of steps you successfully took to get to the solution. 
	 * Print out this number of steps and the solution.
	 */
	private SudokuBoard board;
	private int successfulSteps = 0;

	public GradientDescent(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Square> unsolvedSquares = board.getRows().stream()
				.flatMap(List::stream)
				.filter(s -> !s.getValue().isPresent())
				.collect(Collectors.toList());
		performGradientSearch(unsolvedSquares);
		System.out.println("Gradient Descent Successfully Converged!");
		System.out.println(String.format("Successful Steps: %s", successfulSteps));
		board.print();
	}

	private void performGradientSearch(List<@NonNull Square> unsolvedSquares) {
		while(!board.isSolved()) {
			unsolvedSquares.stream().findFirst();
		}
	}
}
