package com.ai.sudoku;

import java.io.File;
import java.util.Scanner;

public class AISudokuMain {

	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\Joey B\\git\\ArtificialIntelligence\\AISudoku\\test\\boardA.sudoku";
		SudokuBoardBuilder boardBuilder = new SudokuBoardBuilder();
		try (Scanner scanner = new Scanner(new File(filePath))) {
			int size = scanner.nextInt();
			int boxQtyX = scanner.nextInt();
			int boxQtyY = scanner.nextInt();
			scanner.nextLine();
			SudokuBoard board = boardBuilder.build(scanner, size, boxQtyX, boxQtyY);
			SudokuAIHandler sudoku = new SudokuAIHandler(board);
			sudoku.runArcConsistancy();
			sudoku.runGeneticAlgorithm();
			sudoku.runArcConsistancy();
		}
	}
}
