package com.ai.sudoku.constraint;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.google.common.collect.Sets;

public abstract class BinaryConstraint extends Constraint {
	
	@NonNull private Square squareA;
	@NonNull private Square squareB;
	@NonNull private BiFunction<@NonNull Square, @NonNull Square, Boolean> biFunc;
	private boolean satisfied;

	protected BinaryConstraint(@NonNull Square cellA, @NonNull Square cellB,
			@NonNull BiFunction<@NonNull Square, @NonNull Square, Boolean> biFunc) {
		this.squareA = cellA;
		this.squareB = cellB;
		this.biFunc = biFunc;
		getSquares().forEach(c -> c.addConstraint(this));
	}

	public boolean isSatisfied() {
		return satisfied;
	}
	
	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}
	
	@Override
	public Set<@NonNull Square> getSquares() {
		return Sets.newHashSet(squareA, squareB);
	}

	@Override
	public boolean invoke() {
		setSatisfied(true);
		return biFunc.apply(squareA, squareB);
	}
	
	@NonNull
	public Square getSquareA() {
		return squareA;
	}
	
	@NonNull
	public Square getSquareB() {
		return squareB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + biFunc.hashCode();
		result = prime * result + squareA.hashCode();
		result = prime * result + squareB.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof BinaryConstraint)) return false;
		BinaryConstraint other = (BinaryConstraint) obj;
		if (squareA == other.squareA) {
			if (squareB == other.squareB) {
				return biFunc == other.biFunc;
			}
		}
		return false;
	}
}
