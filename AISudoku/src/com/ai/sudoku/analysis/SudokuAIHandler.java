package com.ai.sudoku.analysis;

import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.exception.InvalidBoardException;

public class SudokuAIHandler {

	private SudokuBoard board;

	public SudokuAIHandler(SudokuBoard board) {
		this.board = board;
	}

	public void runArcConsistancy() throws InvalidBoardException {
		System.out.println("Started Arc Consistency Analysis");
		ArcConsistancyAnalyzer analyzer = new ArcConsistancyAnalyzer(board);
		analyzer.run();
		System.out.println("Completed Arc Consistency Analysis");
	}

	public void runGeneticAlgorithm() {
		// TODO Auto-generated method stub
		
	}

}
