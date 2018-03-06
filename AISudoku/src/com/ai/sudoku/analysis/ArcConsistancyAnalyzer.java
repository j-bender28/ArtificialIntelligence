package com.ai.sudoku.analysis;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.constraint.Constraint;
import com.ai.sudoku.constraint.InequalityConstraint;
import com.google.common.collect.Lists;

public class ArcConsistancyAnalyzer {

	private SudokuBoard board;

	public ArcConsistancyAnalyzer(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Constraint> constraints = extractConstraints();
		while (!constraints.stream().allMatch(Constraint::isSatisfied)) {
			for (Constraint constraint : constraints) {
				if (!constraint.isSatisfied()) {
					constraint.invoke();
				}
			}
		}
	}

	private List<@NonNull Constraint> extractConstraints() {
		List<@NonNull Constraint> constraints = Lists.newArrayList();
		for (Collection<@NonNull Square> cellGroup : board.getAllGroups()) {
			for (Square cellA : Lists.newArrayList(cellGroup)) {
				for (Square cellB : cellGroup) {
					if (!cellA.equals(cellB)) {
						constraints.add(new InequalityConstraint(cellA, cellB));
					}
				}
			}
		}
		return constraints;
	}
}
