package com.ai.sudoku;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class SudokuBoardBuilder {

	public SudokuBoard build(Scanner scanner, int size, int rowsPerBox, int colsPerBox) throws IOException {
		Multimap<Integer, Cell> groups = ArrayListMultimap.create(size, size);
		int rowAdder = (int) Math.pow(size, 1);
		int colAdder = (int) Math.pow(size, 2);
		int boxAdder = (int) Math.pow(size, 3);
		int cellIndex = 0;
		while (scanner.hasNextInt()) {
			int rowInd = cellIndex / size;
			int colInd = cellIndex % size;
			int box = (rowInd / rowsPerBox) * rowsPerBox + colInd / colsPerBox;
			Cell cell = new Cell(scanner.nextInt());
			groups.put(rowInd + rowAdder, cell);
			groups.put(colInd + colAdder, cell);
			groups.put(box + boxAdder, cell);
			cellIndex++;
		}
		return new SudokuBoard(Lists.newArrayList(groups.asMap().values()), size);
	}
}
