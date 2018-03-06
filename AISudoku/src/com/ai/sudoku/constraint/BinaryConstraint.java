package com.ai.sudoku.constraint;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.google.common.collect.Sets;

public abstract class BinaryConstraint extends Constraint {
	
	@NonNull private Square cellA;
	@NonNull private Square cellB;
	@NonNull private BiFunction<com.ai.sudoku.board.Square, com.ai.sudoku.board.Square, Boolean> biFunc;

	protected BinaryConstraint(@NonNull Square cellA, @NonNull Square cellB,
			@NonNull BiFunction<@NonNull Square, @NonNull Square, Boolean> biFunc) {
		this.cellA = cellA;
		this.cellB = cellB;
		this.biFunc = biFunc;
		getCells().forEach(c -> c.addConstraint(this));
	}
	
	@Override
	public Set<@NonNull Square> getCells() {
		return Sets.newHashSet(cellA, cellB);
	}

	@Override
	public boolean invoke() {
		setSatisfied(true);
		return biFunc.apply(cellA, cellB);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + biFunc.hashCode();
		result = prime * result + cellA.hashCode();
		result = prime * result + cellB.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(obj instanceof BinaryConstraint)) return false;
		BinaryConstraint other = (BinaryConstraint) obj;
		if (cellA == other.cellA) {
			if (cellB == other.cellB) {
				return biFunc == other.biFunc;
			}
		}
		return false;
	}	
}
