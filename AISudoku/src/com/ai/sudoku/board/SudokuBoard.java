package com.ai.sudoku.board;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class SudokuBoard {

	private Cluster[] rows;
	private Set<Constraint> constraints;

	public SudokuBoard(Cluster[] rows, Set<Constraint> constraints) {
		this.rows = rows;
		this.constraints = constraints;
	}

	public Set<Constraint> getConstraints() {
		return constraints;
	}

	public List<Cluster> getRows() {
		return Lists.newArrayList(rows);
	}

	public void resetGuesses() {
		Stream.of(rows).flatMap(row -> row.stream()).forEach(Square::resetGuess);
		constraints.forEach(c -> c.setSatisfied(false));		
	}
	
	public boolean isSolved() {
		for (Cluster row : rows) {
			for (Square cell : row) {
				if (!cell.getBestGuess().isPresent()) {
					return false;
				}
			}
		}
		return true;
	}

	public void print() {
		System.out.println();
		for (Cluster row : rows) {
			for (Square cell : row) {
				cell.print();
				System.out.print("//");
			}
			System.out.println();
		}
		System.out.println();
	}

	public boolean containsUnsatisfiedConstraints() {
		return !constraints.stream().allMatch(Constraint::isSatisfied);
	}

	public int getDomain() {
		return rows.length;
	}
}
