package com.ai.sudoku.board;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

public class SudokuBoardBuilder {

	public SudokuBoard build(Scanner scanner, int size, int rowsPerBox, int colsPerBox) throws IOException {
		ArrayListMultimap<Integer, @NonNull Square> rows = ArrayListMultimap.create(size, size);
		ArrayListMultimap<Integer, @NonNull Square> cols = ArrayListMultimap.create(size, size);
		ArrayListMultimap<Integer, @NonNull Square> boxes = ArrayListMultimap.create(size, size);
		int cellIndex = 0;
		while (scanner.hasNextInt()) {
			int rowInd = cellIndex / size;
			int colInd = cellIndex % size;
			int box = (rowInd / rowsPerBox) * rowsPerBox + colInd / colsPerBox;
			int val = scanner.nextInt();		
			Square cell;
			if (val > 0 && val <= size) {
				cell = new Square(val);
			} else {
				cell = new Square(createPossibilities(size));
			}
			rows.put(rowInd, cell);
			cols.put(colInd, cell);
			boxes.put(box, cell);
			cellIndex++;
		}
		return new SudokuBoard(rows.asMap().values(), 
				cols.asMap().values(), 
				boxes.asMap().values(),
				size);
	}

	private List<Integer> createPossibilities(int size) {
		List<Integer> possibilities = Lists.newArrayListWithCapacity(size);
		for (int i = 1; i <= size; i++) {
			possibilities.add(i);
		}
		return possibilities;
	}
}
