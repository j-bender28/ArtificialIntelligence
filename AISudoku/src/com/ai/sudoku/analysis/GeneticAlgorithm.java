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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

public class GeneticAlgorithm {

	private static final int POPULATION_SIZE = 100;
	private SudokuBoard board;

	public GeneticAlgorithm(SudokuBoard board) {
		this.board = board;
	}

	public void run() {
		List<@NonNull Square> squares = board.getRows().stream().flatMap(List::stream).collect(Collectors.toList());
		List<int[]> population = randomRestartPopulation(squares);
		double bestEval = 0;
		int maxEvalNotExceededCount = 0;
		while (true) {
			if (maxEvalNotExceededCount == 100) {
				population = randomRestartPopulation(squares);
				maxEvalNotExceededCount = 0;
			}
			try {
				double currentEval = runGeneticAlgorithm(-1, population, squares);
				if (currentEval > bestEval) {
					System.out.println(String.format("Evaluation Improved, Was %s, is %s", bestEval, currentEval));
					bestEval = currentEval;
					maxEvalNotExceededCount = 0;
				} else {
					maxEvalNotExceededCount++;
				}
			} catch (BoardSolvedException e) {
				break;
			}
		}
		System.out.println("Genetic Algorithm Complete!");
		board.print();
	}

	private List<int[]> randomRestartPopulation(List<@NonNull Square> squares) {
		List<int[]> population = Lists.newArrayListWithCapacity(POPULATION_SIZE);
		Stream.generate(() -> generateRandomBoardValues(squares)).limit(POPULATION_SIZE).forEach(population::add);
		return population;
	}

	private double runGeneticAlgorithm(double bestConvergence, List<int[]> population, List<@NonNull Square> squares)
			throws BoardSolvedException {
		ArrayListMultimap<Double, int[]> probValues = calculateProbValues(population, squares);
		double maxEval = probValues.keySet().stream().max(Comparator.naturalOrder()).get();
		List<int[][]> popPairs = determinePopulationPairs(probValues);
		for (int[][] popPair : popPairs) {
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
		return maxEval;
	}
	
	private List<int[][]> determinePopulationPairs(ArrayListMultimap<Double, int[]> probValues) {
		List<int[][]> popPairings = Lists.newArrayListWithCapacity(POPULATION_SIZE / 2);
		while (popPairings.size() < 50) {
			Optional<Entry<Double, int[]>> boardEntryA = probValues.entries().stream().skip(getRandomInt(0, probValues.entries().size())).findFirst();
			Optional<Entry<Double, int[]>> boardEntryB = probValues.entries().stream().skip(getRandomInt(0, probValues.entries().size())).findFirst();			
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

	private ArrayListMultimap<Double, int[]> calculateProbValues(List<int[]> population, final List<@NonNull Square> squares) throws BoardSolvedException {
		ArrayListMultimap<Double, int[]> probValues = ArrayListMultimap.create(population.size(), 10);
		List<Constraint> constraints = board.getConstraints();
		for (int[] boardVals : population) {
			for (int index = 0; index < boardVals.length; index++) {
				squares.get(index).setBestGuess(boardVals[index]);
			}
			int violationCount = (int) constraints.stream().filter(Constraint::isViolated).count();
			if (violationCount == 0) throw new BoardSolvedException();
			double evalFunc = 1 - ((double) violationCount) / constraints.size();
			if (evalFunc > 0.97) {
//				board.print();
			}
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

	private int getRandomInt(int low, int high) {
		return new Random().ints(low, high).limit(1).findFirst().getAsInt();
	}
}
