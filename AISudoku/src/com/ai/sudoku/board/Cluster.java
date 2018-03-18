package com.ai.sudoku.board;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

public class Cluster extends ArrayList<@NonNull Square> {
	
	private static final long serialVersionUID = 1L;

	public Cluster(int size) {
		super(size);
	}
}
