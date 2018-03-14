package com.ai.sudoku.board;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SudokuBoardBuilder {

	public SudokuBoard buildBoard(Scanner scanner, int size, int rowsPerBox, int colsPerBox) throws IOException, InvalidBoardException {
		ArrayListMultimap<Integer, @NonNull Square> rows = ArrayListMultimap.create(size, size);
		ArrayListMultimap<Integer, @NonNull Square> cols = ArrayListMultimap.create(size, size);
		ArrayListMultimap<Integer, @NonNull Square> boxes = ArrayListMultimap.create(size, size);
		int cellIndex = 0;
		while (scanner.hasNextInt()) {
			int rowInd = cellIndex / size;
			int colInd = cellIndex % size;
			int box = (rowInd / rowsPerBox) * rowsPerBox + colInd / colsPerBox;
			int val = scanner.nextInt();		
			Square square;
			if (val > 0 && val <= size) {
				square = new Square(rowInd, colInd, val);
			} else {
				square = new Square(rowInd, colInd, createPossibilities(size));
			}
			rows.put(rowInd, square);
			cols.put(colInd, square);
			boxes.put(box, square);
			cellIndex++;
		}
		List<Collection<@NonNull Square>> rowList = list(rows.asMap().values());
		List<Collection<@NonNull Square>> allGroups = list(rowList, cols.asMap().values(), boxes.asMap().values());
		ensureValidInputBoard(allGroups, size);
		List<Constraint> constraints = extractConstraints(allGroups);
		return new SudokuBoard(rowList, constraints);
	}

	private void ensureValidInputBoard(List<Collection<@NonNull Square>> allGroups, int size) throws InvalidBoardException {
		for (Collection<@NonNull Square> group : allGroups) {
			List<Integer> allVals = group.stream()
					.map(square -> square.getValue())
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());
			if (allVals.size() != Sets.newHashSet(allVals).size()) {
				throw new InvalidBoardException();
			}
		}
	}

	private <T> List<Collection<@NonNull T>> list(Collection<Collection<@NonNull T>> ...values) {
		return Stream.of(values)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	private List<Constraint> extractConstraints(List<Collection<@NonNull Square>> allGroups) {
		LinkedHashSet<Constraint> constraints = Sets.newLinkedHashSet();
		for (Collection<@NonNull Square> cellGroup : allGroups) {
			for (Square cellA : cellGroup) {
				for (Square cellB : cellGroup) {
					if (!cellA.equals(cellB)) {
						Constraint constraint = new InequalityConstraint(cellA, cellB);
						if (constraints.add(constraint)) {
							cellA.addConstraint(constraint);
							cellB.addConstraint(constraint);
						}
					}
				}
			}
		}
//		allGroups.forEach(group -> constraints.add(new SinglePossibilityConstraint(group)));
		return Lists.newArrayList(constraints);
	}

	private List<Integer> createPossibilities(int size) {
		List<Integer> possibilities = Lists.newArrayListWithCapacity(size);
		for (int i = 1; i <= size; i++) {
			possibilities.add(i);
		}
		return possibilities;
	}
}
