package com.ai.sudoku;

public final class Cell {
	
	private int value;

	Cell(int value, int overallSize) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
