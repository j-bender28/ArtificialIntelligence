package com.ai.sudoku.analysis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;
import com.google.common.collect.Lists;

public class BacktrackingSearch {

	private SudokuBoard board;

	public BacktrackingSearch(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		TreeNode root = createBacktrackingTree();
		searchTreeForSolution(root);
		board.print();
	}

	private void searchTreeForSolution(TreeNode parent) {
		for (TreeNode child : parent.getChildren()) {
			board.getConstraints().forEach(c -> c.setSatisfied(true));
			if (child.guessDoesNotViolateConstraints()) {
				board.print();
				searchTreeForSolution(child);
			} else {
				child.revertGuess();
			}
		}
	}

	private TreeNode createBacktrackingTree() {
		TreeNode rootNode = new TreeNode(0, null);
		List<@NonNull Square> unconsumedSquares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		setupChildNodes(rootNode, unconsumedSquares, true);
		return rootNode;
	}

	private void setupChildNodes(TreeNode currentNode, List<@NonNull Square> unconsumedSquares, boolean isFirstRun) {
		if (!unconsumedSquares.isEmpty()) {
			Square square = unconsumedSquares.remove(0);
			Optional<Integer> value = square.getValue();
			for (int guess = 1; guess <= board.getDomain(); guess++) {
				if (value.isPresent() && value.get() != guess) continue; //Don't make branch when square's domain is known
				TreeNode child = new TreeNode(guess, square);
				currentNode.addChild(child);
				setupChildNodes(child, Lists.newArrayList(unconsumedSquares), false);
			}
		}
	}
}
