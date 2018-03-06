package com.ai.sudoku.board;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.constraint.InequalityConstraint;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SudokuBoardBuilder {
	
	public SudokuBoard copyBoard(@NonNull SudokuBoard board) {
		Map<Square, @NonNull Square> squareMapping = Maps.newHashMap();
		ArrayListMultimap<Integer, @NonNull Square> rows = ArrayListMultimap.create();
		int rowInd = 0;
		for (Collection<@NonNull Square> row : board.getRows()) {
			for (Square square : row) {
				Square clonedSquare = new Square(square);
				squareMapping.put(square, clonedSquare);
				rows.put(rowInd, clonedSquare);
			}
			rowInd++;
		}
		List<@NonNull InequalityConstraint> constraints = Lists.newArrayList();
		for(InequalityConstraint constraint : board.getConstraints()) {
			@NonNull Square squareA = squareMapping.get(constraint.getSquareA());
			@NonNull Square squareB = squareMapping.get(constraint.getSquareB());
			constraints.add(new InequalityConstraint(squareA, squareB));
		}
		return new SudokuBoard(rows.asMap().values(), constraints);
	}

	public SudokuBoard buildBoard(Scanner scanner, int size, int rowsPerBox, int colsPerBox) throws IOException {
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
		List<Collection<@NonNull Square>> rowList = list(rows.asMap().values());
		List<Collection<@NonNull Square>> allGroups = list(rowList, cols.asMap().values(), boxes.asMap().values());
		List<@NonNull InequalityConstraint> constraints = extractConstraints(allGroups);
		return new SudokuBoard(rowList, constraints);
	}

	private <T> List<Collection<@NonNull T>> list(Collection<Collection<@NonNull T>> ...values) {
		return Stream.of(values)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private List<@NonNull InequalityConstraint> extractConstraints(List<Collection<@NonNull Square>> allGroups) {
		List<@NonNull InequalityConstraint> constraints = Lists.newArrayList();
		for (Collection<@NonNull Square> cellGroup : allGroups) {
			for (Square cellA : Lists.newArrayList(cellGroup)) {
				for (Square cellB : cellGroup) {
					if (!cellA.equals(cellB)) {
						constraints.add(new InequalityConstraint(cellA, cellB));
					}
				}
			}
		}
		return constraints;
	}

	private List<Integer> createPossibilities(int size) {
		List<Integer> possibilities = Lists.newArrayListWithCapacity(size);
		for (int i = 1; i <= size; i++) {
			possibilities.add(i);
		}
		return possibilities;
	}
}
