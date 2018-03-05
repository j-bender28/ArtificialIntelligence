package com.ai.sudoku;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

public class ArcConsistancyAnalyzer {

	private SudokuBoard board;

	public ArcConsistancyAnalyzer(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		Set<Supplier<Boolean>> binaryConstraints = extractBinaryConstraints();

	}

	private Set<Supplier<Boolean>> extractBinaryConstraints() {
		Set<Supplier<Boolean>> binaryConstraints = Sets.newHashSet();
		for (Collection<Cell> cellGroup : board.getGroups()) {
			for (Cell cellA : cellGroup) {
				binaryConstraints.add(() -> 0 < cellA.getValue() && cellA.getValue() < board.getSize());
				for (Cell cellB : cellGroup) {
					if (!cellA.equals(cellB)) {
						binaryConstraints.add(() -> !cellA.equals(cellB));
					}
				}
			}
		}
		return binaryConstraints;
	}
}
