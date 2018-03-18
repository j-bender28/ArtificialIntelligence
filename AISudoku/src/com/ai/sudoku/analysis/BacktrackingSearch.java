package com.ai.sudoku.analysis;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;

public class BacktrackingSearch {

	private SudokuBoard board;

	public BacktrackingSearch(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		TreeNode root = createBacktrackingTree();
		for (TreeNode child : root.getChildren()) {
			
		}
	}

	private TreeNode createBacktrackingTree() {
		TreeNode rootNode = new TreeNode(null);
		List<@NonNull Square> unconsumedSquares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		setupChildNodes(rootNode, unconsumedSquares);
		return rootNode;
	}

	private void setupChildNodes(TreeNode currentNode, List<@NonNull Square> unconsumedSquares) {
		if (!unconsumedSquares.isEmpty()) {
			Square square = unconsumedSquares.remove(0);
			for (int i = 1; i <= board.getDomain(); i++) {				
				TreeNode child = new TreeNode(square);
				currentNode.addChild(child);
				setupChildNodes(child, unconsumedSquares);
			}
		}
	}
}
