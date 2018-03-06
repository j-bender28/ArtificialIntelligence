package com.ai.sudoku.board;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.constraint.InequalityConstraint;
import com.google.common.collect.Lists;

public class SudokuBoard {

	private Collection<Collection<@NonNull Square>> rows;
	private List<@NonNull InequalityConstraint> constraints;

	public SudokuBoard(Collection<Collection<@NonNull Square>> rows, List<@NonNull InequalityConstraint> constraints) {
		this.rows = rows;
		this.constraints = constraints;
	}

	public List<@NonNull InequalityConstraint> getConstraints() {
		return constraints;
	}

	public List<Collection<@NonNull Square>> getRows() {
		return Lists.newArrayList(rows);
	}

	public void print() {
		for (Collection<@NonNull Square> row : rows) {
			for (Square cell : row) {
				Optional<Integer> value = cell.getValue();
				if (value.isPresent()) {
					System.out.print(value.get());
				} else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
	}
}
