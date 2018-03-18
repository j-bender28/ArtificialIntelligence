package com.ai.sudoku.board;

public class BoardLocation {

	private int rowIndex;
	private int colIndex;

	public BoardLocation(int rowIndex, int colIndex) {
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}
	
	@Override
	public String toString() {
		return String.format("[%s,%s]", rowIndex, colIndex);
	}
}
