package com.ai.sudoku.analysis;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.constraint.Constraint;
import com.ai.sudoku.constraint.InequalityConstraint;

public class ArcConsistancyAnalyzer {

	private SudokuBoard board;

	public ArcConsistancyAnalyzer(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull InequalityConstraint> constraints = board.getConstraints();
		while (!constraints.stream().allMatch(Constraint::isSatisfied)) {
			for (Constraint constraint : constraints) {
				if (!constraint.isSatisfied()) {
					constraint.invoke();
				}
			}
		}
	}
}
