package com.ai.sudoku.main;

import java.io.File;
import java.util.Scanner;

import com.ai.sudoku.analysis.SudokuAIHandler;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.board.SudokuBoardBuilder;

public class AISudokuMain {

	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\Joey B\\git\\ArtificialIntelligence\\AISudoku\\test\\boardA.sudoku";
		SudokuBoardBuilder boardBuilder = new SudokuBoardBuilder();
		try (Scanner scanner = new Scanner(new File(filePath))) {
			int size = scanner.nextInt();
			int boxQtyX = scanner.nextInt();
			int boxQtyY = scanner.nextInt();
			scanner.nextLine();
			SudokuBoard board = boardBuilder.buildBoard(scanner, size, boxQtyX, boxQtyY);
			SudokuAIHandler sudoku = new SudokuAIHandler(board);
			sudoku.runArcConsistancy();
			sudoku.runGeneticAlgorithm();
		}
	}
}
