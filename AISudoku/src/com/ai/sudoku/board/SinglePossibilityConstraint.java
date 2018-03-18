package com.ai.sudoku.board;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.exception.InvalidBoardException;

public class SinglePossibilityConstraint extends Constraint {

	public SinglePossibilityConstraint(Collection<@NonNull Square> squares) {
		super(squares.stream().toArray(Square[]::new));
		squares.forEach(s -> s.addConstraint(this));
	}

	@Override
	public boolean invoke(boolean isGuess) throws InvalidBoardException {
		for (Square square : squares) {
			List<Integer> sqPoss = square.getPossibilities();
			Stream.of(squares)
				.filter(s -> !s.equals(square))
				.flatMap(s -> s.getPossibilities().stream())
				.forEach(sqPoss::remove);
			if (sqPoss.size() == 1) {
				int value = sqPoss.get(0);
				if (isGuess) {
					square.setBestGuess(value);
				} else {
					square.setValue(value);
				}
				return true;
				
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Constraint between");
		for (Square square : squares) {
			builder.append(String.format(" Square%s", square.getLocation()));
		}
		return builder.toString();
	}
}
