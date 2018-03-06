package com.ai.sudoku.constraint;

import java.util.Set;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.google.common.collect.Sets;

public abstract class UnaryConstraint extends Constraint {
	
	@NonNull private Square cell;
	private Function<@NonNull Square, Boolean> unFunc;

	protected UnaryConstraint(@NonNull Square cell, Function<@NonNull Square,Boolean> unFunc) {
		this.cell = cell;
		this.unFunc = unFunc;
	}

	@Override
	public boolean invoke() {
		return unFunc.apply(cell);
	}
	
	public Set<@NonNull Square> getCells() {
		return Sets.newHashSet(cell);
	}
}
