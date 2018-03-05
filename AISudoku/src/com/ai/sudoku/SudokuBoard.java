package com.ai.sudoku;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class SudokuBoard {

	private List<Collection<Cell>> groups;
	private int size;

	public SudokuBoard(List<Collection<Cell>> cellGroups, int size) {
		this.groups = cellGroups;
		this.size = size;
	}

	public List<Collection<Cell>> getGroups() {
		return Lists.newArrayList(groups);
	}

	public int getSize() {
		return size;
	}	
}
