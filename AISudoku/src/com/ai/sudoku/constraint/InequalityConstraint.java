package com.ai.sudoku.constraint;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;

public class InequalityConstraint extends BinaryConstraint {

	public InequalityConstraint(@NonNull Square cellA, @NonNull Square cellB) {
		super(cellA, cellB, (c1, c2) -> c1.invokeInequalityConstraint(c2));
	}
}
