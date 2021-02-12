package wwwordz.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wwwordz.server.Trie.Search;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;
import wwwordz.shared.Puzzle.Solution;

public class Generator {
	private static final Dictionary dictionary = Dictionary.getInstance();
	public Generator() {
		
	}
	public Puzzle generate() {
		Puzzle puzzle = new Puzzle();
		Table table = new Table();
		while (table.getEmptyCells().size() != 0) {
			generateHelper(table);
		}
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}

	public Puzzle random() {
		Puzzle puzzle = randomHelper();
		return puzzle;
	}

	public static List<Solution> getSolutions(Table table) {
		List<Solution> solutionList = new ArrayList<Solution>();
		for (Cell cell : table) {
			StringBuffer string = new StringBuffer();
			List<Cell> used = new ArrayList<Cell>();
			Search search = dictionary.startSearch();
			getSolutions(table, cell, string, solutionList, used, search);
		}

		return solutionList;
	}

	// Function to create a Table with a random large word from the trie
	private Table generateHelper(Table table) {
		Random random = new Random();
		String string = dictionary.getRandomLargeWord();

		// Choose Random cell to start inserting the string in the puzzle
		List<Cell> emptyList = table.getEmptyCells();
		int rand = random.nextInt(emptyList.size());
		Cell cell = emptyList.get(rand);
		generateHelper(table, string, 0, cell);
		return table;

	}

	// Auxiliary recursive function that inserts the letters of a word randomly in
	// the table
	private void generateHelper(Table table, String string, int index, Cell cell) {
		Random random = new Random();
		if (index >= string.length())
			return;
		if (table.getEmptyCells().size() == 0)
			return;

		List<Cell> neighbors = table.getNeighbors(cell);

		// Chosse random neighbor
		cell = neighbors.get(random.nextInt(neighbors.size()));
		table.setLetter(cell.getRow(), cell.getColumn(), string.charAt(index));
		index++;
		generateHelper(table, string, index, cell);

	}

	// Function to insert random letters in a Table
	private Puzzle randomHelper() {
		int SIZE = 4;
		String data[] = new String[SIZE];

		for (int i = 0; i < SIZE; i++) {
			StringBuilder string = new StringBuilder();
			for (int c = 0; c < SIZE; c++) {
				string.append(randomLetter());
			}
			data[i] = string.toString();
		}

		Table table = new Table(data);
		Puzzle puzzle = new Puzzle(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;

	}

	// Function that returns a random letter
	private char randomLetter() {
		Random random = new Random();
		char letter = (char) ('A' + random.nextInt('Z' - 'A'));
		return letter;

	}

	// Recursive function that traverses the table and finds the words in the table
	private static void getSolutions(Table table, Cell cell, StringBuffer string, List<Solution> solutionList,
			List<Cell> used, Search search) {

		// boolean append = true;
		if (used.contains(cell))
			return;
		if (!search.continueWith(cell.getLetter()))
			return;

		used.add(cell);
		string.append(cell.getLetter());

		if (search.isWord() && !alreadyDiscovered(solutionList, string)) {
		
			Solution solution = new Solution(string.toString(), used);
			solutionList.add(solution);
		}

		for (Cell neighbor : table.getNeighbors(cell)) {
			Search searchCopy = new Search(search);
			List<Cell> usedCopy = new ArrayList<Cell>(used);
			StringBuffer stringCopy = new StringBuffer(string);
			getSolutions(table, neighbor, stringCopy, solutionList, usedCopy, searchCopy);

		}

	}

	// Function to verify if a given word is already discovered
	private static boolean alreadyDiscovered(List<Solution> list, StringBuffer string) {
		String word = string.toString();
		for (Solution solution : list) {
			if (solution.getWord().equals(word))
				return true;
		}
		return false;
	}

}
