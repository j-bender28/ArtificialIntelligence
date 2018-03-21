package com.ai.sudoku.main;

import java.io.IOException;

import com.ai.sudoku.analysis.ArcConsistancy;
import com.ai.sudoku.analysis.BacktrackingSearch;
import com.ai.sudoku.analysis.GeneticAlgorithm;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.board.SudokuBoardBuilder;
import com.ai.sudoku.exception.InvalidBoardException;

public class AISudokuMain {
	
	private static final String TEST_FILE = "/board6ASolved.sudoku";

	public static void main(String[] args) throws Exception {		
		System.out.println("Started Arc Consistency Analysis");
		ArcConsistancy acAnalyzer = new ArcConsistancy(createBoard());
		acAnalyzer.run();
		System.out.println();
		
		System.out.println("Started Backtracking Search Analysis");
		BacktrackingSearch search = new BacktrackingSearch(createBoard());
		search.run();
		System.out.println();
		
		System.out.println("Started Genetic Algorithm Analysis");
		GeneticAlgorithm genAlg = new GeneticAlgorithm(createBoard());
		genAlg.run();
		System.out.println();
	}

	private static SudokuBoard createBoard() throws IOException, InvalidBoardException {
		SudokuBoardBuilder boardBuilder = new SudokuBoardBuilder();
		SudokuBoard board = boardBuilder.buildBoard(TEST_FILE);
		return board;
	}
}
