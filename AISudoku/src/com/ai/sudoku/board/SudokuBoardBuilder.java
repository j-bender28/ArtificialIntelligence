package com.ai.sudoku.board;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SudokuBoardBuilder {

	public SudokuBoard buildBoard(String filePath) throws IOException, InvalidBoardException {
		try (Scanner scanner = new Scanner(getClass().getResourceAsStream(filePath))) {
			int size = scanner.nextInt();
			int rowsPerBox = scanner.nextInt();
			int colsPerBox = scanner.nextInt();
			scanner.nextLine();
			return buildBoard(scanner, size, rowsPerBox, colsPerBox);
		}
	}

	private SudokuBoard buildBoard(Scanner scanner, int size, int rowsPerBox, int colsPerBox) throws IOException, InvalidBoardException {
		Cluster[] rows = createClusterArray(size);
		Cluster[] cols = createClusterArray(size);
		Cluster[] boxes = createClusterArray(size);
		int cellIndex = 0;
		while (scanner.hasNextInt()) {
			int rowInd = cellIndex / size;
			int colInd = cellIndex % size;
			int boxInd = (rowInd / rowsPerBox) * rowsPerBox + colInd / colsPerBox;
			int val = scanner.nextInt();
			BoardLocation boardLoc = new BoardLocation(rowInd, colInd);
			Square square;
			if (val > 0 && val <= size) {
				square = new Square(boardLoc, val);
			} else {
				square = new Square(boardLoc, createPossibilities(size));
			}
			rows[rowInd].add(square);
			cols[colInd].add(square);
			boxes[boxInd].add(square);
			cellIndex++;
		}
		List<Cluster> allClusters = Stream.of(rows, cols, boxes).flatMap(Stream::of).collect(Collectors.toList());
		ensureValidInputBoard(allClusters, size);
		List<Constraint> constraints = extractConstraints(allClusters);
		return new SudokuBoard(rows, constraints);
	}

	private Cluster[] createClusterArray(int size) {
		return Stream.generate(() -> new Cluster(size)).limit(size).toArray(Cluster[]::new);
	}

	private void ensureValidInputBoard(List<Cluster> allClusters, int size) throws InvalidBoardException {
		for (Cluster cluster : allClusters) {
			List<Integer> allVals = cluster.stream()
					.map(square -> square.getValue())
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());
			if (allVals.size() != Sets.newHashSet(allVals).size()) {
				throw new InvalidBoardException();
			}
		}
	}

	private List<Constraint> extractConstraints(List<Cluster> allClusters) {
		LinkedHashSet<Constraint> constraints = Sets.newLinkedHashSet();
		for (Cluster cluster : allClusters) {
			for (Square cellA : cluster) {
				for (Square cellB : cluster) {
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
