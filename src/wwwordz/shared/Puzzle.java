package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Puzzle implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Solution> solutions;
	private Table table;

	// Constructor
	public Puzzle() {
		solutions = new ArrayList<Solution>();
		table = new Table();

	}

	public Puzzle(Table table) {
		this.table = table;
	}

	// Getters and Setters

	public Table getTable() {
		return table;
	}
	
	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	public boolean containsWord(String string) {
		for(Solution sol : solutions) {
			String temp = sol.getWord();
			if(temp.equals(string))
				return true;
			
		}
		return false;
	}

	public static class Solution implements Serializable {
		private static final long serialVersionUID = 1L;
		private List<Table.Cell> cells;
		private String word;
		private int points;

		// Constructor
		public Solution() {
			cells = new ArrayList<Table.Cell>();
			word = new String();
			points = 0;

		}
		
		@Override
		public boolean equals(Object o) {
			if(o==this) {
				return true;
			}
			Solution temp = (Solution) o;
			
			if(temp.getWord().equals(getWord()))	
				return true;
			return false;
			
		}

		public Solution(String word, List<Table.Cell> cells) {
			this.word = word;
			this.cells = cells;
			points = calculatePoints(word);
		}

		// Getters and Setters
		
		public List<Table.Cell> getCells() {
			return cells;
		}

		public String getWord() {
			return word;
		}

		public int getPoints() {
			return points;
		}

		public void setCells(List<Table.Cell> cells) {
			this.cells = cells;
		}

		public void setWords(String word) {
			this.word = word;
			points = calculatePoints(word);
		}

		private int calculatePoints(String word) {
			return calculatePoints(word.length());

		}

		private int calculatePoints(int size) {
			if (size == 3)
				return 1;
			return 2 * calculatePoints(size - 1) + 1;
		}

		
		
	}

}
