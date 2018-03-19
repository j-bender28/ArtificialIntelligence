package com.ai.sudoku.analysis;

import java.util.List;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Lists;

public class TreeNode {
	
	private List<TreeNode> childNodes = Lists.newArrayList();
	private Square square;
	private int value;

	TreeNode(int value, Square square) {
		this.value = value;
		this.square = square;
	}

	public void addChild(TreeNode child) {
		childNodes.add(child);
	}

	public List<TreeNode> getChildren() {
		return childNodes;
	}

	public void revertGuess() {
		square.resetGuess();
	}

	public boolean guessDoesNotViolateConstraints() {
		square.setBestGuess(value);
		return !square.getConstraints().stream().anyMatch(Constraint::isViolated);
	}
}
