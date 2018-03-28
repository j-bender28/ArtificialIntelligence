package com.ai.sudoku.board;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Sets;

public abstract class Constraint {

	public abstract boolean invoke(boolean isGuess) throws InvalidBoardException;
	public abstract boolean isViolated();
	private boolean satisfied;
	protected @NonNull Set<Square> squares;
	
	public Constraint(@NonNull Square ...squares) {
		this.squares = Sets.newHashSet(squares);
	}

	public boolean isSatisfied() {
		return satisfied;
	}
	
	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + squares.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof Constraint)) return false;
		Constraint other = (Constraint) obj;
		return squares.equals(other.squares);
	}
}
