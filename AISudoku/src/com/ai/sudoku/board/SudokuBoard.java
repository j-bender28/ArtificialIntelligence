package com.ai.sudoku.board;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;

public class SudokuBoard {

	private Collection<Collection<@NonNull Square>> rows;
	private List<Constraint> constraints;

	public SudokuBoard(Collection<Collection<@NonNull Square>> rows, List<Constraint> constraints) {
		this.rows = rows;
		this.constraints = constraints;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public List<Collection<@NonNull Square>> getRows() {
		return Lists.newArrayList(rows);
	}

	public void resetGuesses() {
		rows.stream().flatMap(row -> row.stream()).forEach(Square::resetGuess);
		constraints.forEach(c -> c.setSatisfied(false));		
	}
	
	public boolean isSolved() {
		for (Collection<@NonNull Square> row : rows) {
			for (Square cell : row) {
				if (!cell.getValue().isPresent()) {
					return false;
				}
			}
		}
		return true;
	}

	public void print() {
		System.out.println();
		for (Collection<@NonNull Square> row : rows) {
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
}
