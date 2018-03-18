package com.ai.sudoku.analysis;

import java.util.List;

import com.ai.sudoku.board.Square;
import com.google.common.collect.Lists;

public class TreeNode {
	
	private List<TreeNode> childNodes = Lists.newArrayList();
	private Square square;

	TreeNode(Square square) {
		this.square = square;
	}

	public void addChild(TreeNode child) {
		childNodes.add(child);
	}

	public List<TreeNode> getChildren() {
		return childNodes;
	}
}
