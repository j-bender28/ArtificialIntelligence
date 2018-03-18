package com.ai.sudoku.board;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.exception.InvalidBoardException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;

public class Square {
	
	private BoardLocation loc;
	private Optional<Integer> value = Optional.empty();
	private Optional<Integer> bestGuest = Optional.empty();
	private FilteredList<Integer> possibilities = new FilteredList<Integer>(FXCollections.observableArrayList());
	private List<Constraint> constraints = Lists.newArrayList();

	protected Square(BoardLocation boardLoc, int value) {
		this(boardLoc, Lists.newArrayList());
		this.value = Optional.of(value);
		this.bestGuest = this.value;
	}
	
	Square(BoardLocation boardLoc, List<Integer> possibilities) {
		this.loc = boardLoc;
		List<Integer> sourcePoss = (List<Integer>) this.possibilities.getSource();
		possibilities.forEach(sourcePoss::add);
		this.possibilities.addListener((ListChangeListener<Integer>) lcl -> {
			constraints.forEach(c -> c.setSatisfied(false));
		});
	}

	protected void addConstraint(@NonNull Constraint constraint) {
		constraints.add(constraint);
	}
	
	public void setBestGuess(int guess) {
		this.bestGuest = Optional.of(guess);
		resetFilter();
	}

	public void setValue(Integer value) {
		this.value = Optional.of(value);
		resetFilter();
		possibilities.getSource().clear();
	}

	public List<Integer> getPossibilities() {
		return Lists.newArrayList(possibilities);
	}
	
	private boolean filterPossibility(Integer value) throws InvalidBoardException {
		Set<Integer> filterKeys = Sets.newHashSet(this.possibilities);
		filterKeys.remove(value);
		int sizeBefore = possibilities.size();
		possibilities.predicateProperty().set(val -> filterKeys.contains(val));
		if (sizeBefore > 0 && possibilities.isEmpty()) {
			throw new InvalidBoardException();
		}
		return sizeBefore != possibilities.size();	
	}
	
	public boolean removePossibility(Integer value, boolean isGuess) throws InvalidBoardException {
		boolean updated = false;
		if (possibilities.contains(value)) {
			if (isGuess) {
				updated = filterPossibility(value);
			} else {
				List<Integer> sourcePoss = (List<Integer>) possibilities.getSource();
				updated = sourcePoss.remove(value);
				if (sourcePoss.size() == 1) {
					setValue(sourcePoss.get(0));
				}
			}
		}		
		return updated;
	}

	public Optional<Integer> getValue() {
		return value;
	}

	public boolean invokeInequalityConstraints(@NonNull Square other, boolean isGuess) throws InvalidBoardException {
		Optional<Integer> valA;
		Optional<Integer> valB;
		if (isGuess) {
			valA = bestGuest;
			valB = other.bestGuest;
		} else {
			valA = value;
			valB = other.value;
		}
		return (valA.isPresent() && other.removePossibility(valA.get(), isGuess)) |
				(valB.isPresent() && removePossibility(valB.get(), isGuess));
	}

	public void resetGuess() {
		bestGuest = Optional.empty();
		resetFilter();
	}
	
	private void resetFilter() {
		possibilities.predicateProperty().set(val -> true);
	}

	public void print() {
		if (value.isPresent()) {
			System.out.print(String.format("   %s  ", value.get()));
		} else {
			for (int i = 0; i < 6; i++) {
				if (possibilities.getSource().contains(i + 1)) {
					System.out.print(i + 1);
				} else {
					System.out.print("-");
				}
			}
		}
	}

	public BoardLocation getLocation() {
		return loc;
	}
	
	@Override
	public String toString() {
		return String.format("Square[%s,%s] >> Value: %s", loc.getRowIndex(), loc.getColIndex(), value);
	}

	public List<Constraint> getConstaints() {
		return constraints;
	}
}
