package com.ai.sudoku.analysis;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

import com.ai.sudoku.board.Constraint;
import com.ai.sudoku.board.Square;
import com.ai.sudoku.board.SudokuBoard;
import com.ai.sudoku.exception.BoardSolvedException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;

/**
 * Attempts to solve the input sudoku board using a genetic algorithm
 * 
 * @author Joe Bender
 * @date Mar 20, 2018
 */
public class GeneticAlgorithm {

	private static final int POPULATION_SIZE = 100;
	private SudokuBoard board;

	public GeneticAlgorithm(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Square> squares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		List<int[]> population = randomRestartPopulation(squares);
		double bestEval = 1;
		int minEvalNotExceededCount = 0;
		int iterCount = 0;
		while (true) {
			iterCount++;
			if (minEvalNotExceededCount == 50) {
				System.out.println(String.format("Genetic Algorithm Failed to Converge! Closest Eval Function: %s", bestEval));
				break;
			}
			try {
				double currentEval = runGeneticAlgorithm(population, squares);
				if (currentEval < bestEval) {
					bestEval = currentEval;
					minEvalNotExceededCount = 0;
				} else {
					minEvalNotExceededCount++;
				}				
			} catch (BoardSolvedException e) {
				System.out.println("Genetic Algorithm Succeessfully Converged!");	
				board.print();
				break;
			}
		}
		System.out.println(String.format("Genetic Algorithm Iterations: %s", iterCount));
	}

	private List<int[]> randomRestartPopulation(List<@NonNull Square> squares) {
		List<int[]> population = Lists.newArrayListWithCapacity(POPULATION_SIZE);
		Stream.generate(() -> generateRandomBoardValues(squares)).limit(POPULATION_SIZE).forEach(population::add);
		return population;
	}

	private double runGeneticAlgorithm(List<int[]> population, List<@NonNull Square> squares)
			throws BoardSolvedException {
		ArrayListMultimap<Double, int[]> probValues = calculateProbValues(population, squares);
		double minEval = probValues.keySet().stream().min(Comparator.naturalOrder()).get();
		for (int[][] popPair : determinePopulationPairs(probValues)) {
			int[] boardA = popPair[0];
			int[] boardB = popPair[1];
			int partitionIndex = getRandomInt(0, boardA.length);			
			for (int index = 0; index < boardA.length; index++) {
				if (!squares.get(index).getValue().isPresent()) {
					if (index <= partitionIndex) {
						int temp = boardA[index];
						boardA[index] = boardB[index];
						boardB[index] = temp;
					}
					boardA[index] = new Random().nextDouble() < 0.01 ? getRandomInt(1, board.getDomain()) : boardA[index];
					boardB[index] = new Random().nextDouble() < 0.01 ? getRandomInt(1, board.getDomain()) : boardB[index];
				}
			}
		}
		return minEval;
	}
	
	private List<int[][]> determinePopulationPairs(ArrayListMultimap<Double, int[]> probValues) {
		List<int[][]> popPairings = Lists.newArrayListWithCapacity(POPULATION_SIZE / 2);
		while (popPairings.size() < POPULATION_SIZE / 2) {
			int[] randInd = findTwoUniqueRandomIndeces(probValues.size());
			Optional<Entry<Double, int[]>> boardEntryA = probValues.entries().stream().skip(randInd[0]).findFirst();
			Optional<Entry<Double, int[]>> boardEntryB = probValues.entries().stream().skip(randInd[1]).findFirst();			
			if (boardEntryA.isPresent() && boardEntryB.isPresent()
					&& boardEntryA.get().getKey() * boardEntryB.get().getKey() < new Random().nextDouble()) {
				double keyA = boardEntryA.get().getKey();
				double keyB = boardEntryB.get().getKey();
				int[] boardValsA = boardEntryA.get().getValue();
				int[] boardValsB = boardEntryB.get().getValue();
				probValues.get(keyA).remove(boardValsA);
				probValues.get(keyB).remove(boardValsB);
				popPairings.add(new int[][] {boardValsA, boardValsB});
			}			
		}		
		return popPairings;
	}

	private int[] findTwoUniqueRandomIndeces(int maxIndex) {
		int[] indeces = new int[2];
		while (indeces[0] == indeces[1]) {
			for (int i = 0; i < 2; i++) {
				indeces[i] = getRandomInt(0, maxIndex);
			}
		}
		return indeces;
	}

	private ArrayListMultimap<Double, int[]> calculateProbValues(List<int[]> population, final List<@NonNull Square> squares) throws BoardSolvedException {
		ArrayListMultimap<Double, int[]> probValues = ArrayListMultimap.create(population.size(), 10);
		List<Constraint> constraints = board.getConstraints();
		for (int[] boardVals : population) {
			for (int index = 0; index < boardVals.length; index++) {
				squares.get(index).setBestGuess(boardVals[index]);
			}
			int violationCount = (int) constraints.stream().filter(Constraint::isViolated).count();
			if (violationCount == 0) throw new BoardSolvedException();
			double evalFunc = ((double) violationCount) / constraints.size();
			probValues.put(evalFunc, boardVals);
		}
		return probValues;
	}

	private int[] generateRandomBoardValues(List<@NonNull Square> squares) {
		int count = (int) Math.pow(board.getDomain(), 2);
		int[] boardVals = new int[count];
		for (int index = 0; index < count; index++) {
			Optional<Integer> value = squares.get(index).getValue();
			if (value.isPresent()) {
				boardVals[index] = value.get();
			} else {
				boardVals[index] = getRandomInt(1, board.getDomain());
			}
		}
		return boardVals;
	}

	private int getRandomInt(int min, int max) {
		return new Random().ints(min, max + 1).limit(1).findFirst().getAsInt();
	}
}
