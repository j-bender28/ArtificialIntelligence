package com.ai.sudoku.main;

import java.io.IOException;

import com.ai.sudoku.analysis.ArcConsistancy;
import com.ai.sudoku.analysis.BacktrackingSearch;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.board.SudokuBoardBuilder;
import com.ai.sudoku.exception.InvalidBoardException;

public class AISudokuMain {
	
	private static final String TEST_FILE = "/boardA.sudoku";

	public static void main(String[] args) throws Exception {		
		ArcConsistancy acAnalyzer = new ArcConsistancy(createBoard());
		acAnalyzer.run();
		
		BacktrackingSearch search = new BacktrackingSearch(createBoard());
		search.run();
	}

	private static SudokuBoard createBoard() throws IOException, InvalidBoardException {
		SudokuBoardBuilder boardBuilder = new SudokuBoardBuilder();
		SudokuBoard board = boardBuilder.buildBoard(TEST_FILE);
		return board;
	}
}
