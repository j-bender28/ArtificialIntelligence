package com.ai.sudoku.board;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.Lists;

public class SudokuBoard {

	private List<Collection<@NonNull Square>> rows;
	private List<Collection<@NonNull Square>> cols;
	private List<Collection<@NonNull Square>> boxes;
	private int size;

	public SudokuBoard(Collection<Collection<@NonNull Square>> rows, 
			Collection<Collection<@NonNull Square>> cols,
			Collection<Collection<@NonNull Square>> boxes, 
			int size) {
		this.rows = Lists.newArrayList(rows);
		this.cols = Lists.newArrayList(cols);
		this.boxes = Lists.newArrayList(boxes);
		this.size = size;
	}

	public List<Collection<@NonNull Square>> getAllGroups() {
		return Stream.of(rows, cols, boxes)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	public int getSize() {
		return size;
	}

	public void print() {
		for (Collection<@NonNull Square> row : rows) {
			for (Square cell : row) {
				Optional<Integer> value = cell.getValue();
				if (value.isPresent()) {
					System.out.print(value.get());
				} else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
	}	
}
