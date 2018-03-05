package com.ai.sudoku;

public class SudokuAIHandler {

	private SudokuBoard board;

	public SudokuAIHandler(SudokuBoard board) {
		this.board = board;
	}

	public void runArcConsistancy() {
		ArcConsistancyAnalyzer analyzer = new ArcConsistancyAnalyzer(board);
		analyzer.run();
	}

	public void runGeneticAlgorithm() {
		// TODO Auto-generated method stub
		
	}

}
