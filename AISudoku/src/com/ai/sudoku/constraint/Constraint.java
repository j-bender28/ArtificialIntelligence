package com.ai.sudoku.constraint;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;

public abstract class Constraint {
	
	public abstract Set<@NonNull Square> getCells();
	public abstract boolean invoke();
	public abstract boolean equals(Object obj);
	public abstract int hashCode();
	
	private boolean satisfied;

	public boolean isSatisfied() {
		return satisfied;
	}
	
	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}
}
