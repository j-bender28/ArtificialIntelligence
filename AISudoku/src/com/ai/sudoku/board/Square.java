package com.ai.sudoku.board;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.constraint.BinaryConstraint;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class Square {
	
	private Optional<Integer> value = Optional.empty();
	private List<Integer> possibilities = Lists.newArrayList();
	private Set<@NonNull BinaryConstraint> constraints = Sets.newHashSet();

	Square(Square square) {
		this.value = square.value;
		this.possibilities = Lists.newArrayList(square.possibilities);
	}
	
	Square(List<Integer> possibilities) {
		this.possibilities = possibilities;
	}

	Square(int value) {
		this.value = Optional.of(value);
	}
	
	public void addConstraint(@NonNull BinaryConstraint constraint) {
		constraints.add(constraint);
	}

	public List<Integer> getPossibilities() {
		return Lists.newArrayList(possibilities);
	}
	
	private boolean removePossibility(Integer value) {
		boolean removed = possibilities.remove(value);
		if (possibilities.size() == 1) {
			this.value = possibilities.stream().findFirst();
		}
		if (removed) {
			constraints.forEach(c -> c.setSatisfied(false));
		}
		return removed;
	}

	public Optional<Integer> getValue() {
		return value;
	}

	public boolean invokeInequalityConstraint(@NonNull Square other) {
		return (value.isPresent() && other.removePossibility(value.get())) |
				(other.value.isPresent() && removePossibility(other.value.get()));
	}
}
