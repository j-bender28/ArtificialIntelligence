package com.ai.sudoku.board;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.exception.InvalidBoardException;

public class InequalityConstraint extends Constraint {

	@NonNull private Square squareA;
	@NonNull private Square squareB;
	
	public InequalityConstraint(@NonNull Square squareA, @NonNull Square squareB) {
		super(squareA, squareB);
		this.squareA = squareA;		
		this.squareB = squareB;
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
	public String toString() {
		return String.format("Constraint between Square%s and Square%s", squareA.getLocation(), squareB.getLocation());
	}

	@Override
	public boolean invoke(boolean isGuess) throws InvalidBoardException {
		return squareA.invokeInequalityConstraints(squareB, isGuess);
	}
}
