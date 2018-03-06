package com.ai.sudoku.constraint;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;

public class InequalityConstraint extends BinaryConstraint {

	public InequalityConstraint(@NonNull Square sqareA, @NonNull Square sqareB) {
		super(sqareA, sqareB, (sA, sB) -> sA.invokeInequalityConstraint(sB));
	}
}
