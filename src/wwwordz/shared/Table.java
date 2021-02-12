package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Table implements Serializable, Iterable<Table.Cell> {
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 4;
	private Cell[][] table = new Cell[SIZE + 2][SIZE + 2];

	public Table() {
		for (int i = 1; i <= SIZE; i++) {
			for (int j = 1; j <= SIZE; j++) {
				table[i][j] = new Cell(i, j, '\0');
			}
		}
	}

	
	public Table(String[] data) {
		int dataIndex = 0;
		for (int i = 1; i <= SIZE; i++) {
			for (int j = 1; j <= SIZE; j++) {
				char letter = data[dataIndex].charAt(j - 1);
				table[i][j] = new Cell(i, j, letter);
			}
			dataIndex++;
		}

	}


	// Getters and Setters
	public Cell getCell(int line, int column) {

		return table[line][column];
	}

	public char getLetter(int line, int column) {
		return table[line][column].getLetter();
	}

	public void setLetter(int row, int column, char letter) {
		table[row][column].setLetter(letter);

	}

	public List<Cell> getEmptyCells() {
		List<Cell> list = new ArrayList<Cell>();
		for (int i = 1; i <= SIZE; i++) {
			for (int j = 1; j <= SIZE; j++) {
				if (table[i][j].isEmpty())
					list.add(table[i][j]);
			}
		}
		return list;
	}

	public List<Cell> getNeighbors(Cell cell) {
		Neighbors neighbors = new Neighbors(cell);
		return neighbors.getNeighbors();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Table [table=" + Arrays.toString(table) + "]";
	}

	public Iterator<Cell> iterator() {
		return new CellIterator();

	}

	public static class Cell implements Serializable  {
		
		private static final long serialVersionUID = 1L;
		int column;
		int row;
		char letter = '\0'; // "Null" character

		// Constructor
		Cell() {
		}

		Cell(int row, int column, char letter) {
			this.row = row;
			this.column = column;
			this.letter = letter;
		}

		Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}

		// Getters and Setters
		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public char getLetter() {
			return letter;
		}

		public void setLetter(char letter) {
			this.letter = letter;
		}

		public boolean isEmpty() {
			if (getLetter() == '\0')
				return true;
			return false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Cell [column=" + column + ", row=" + row + ", letter=" + letter + "]";
		}

	}

	public class Neighbors implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private Cell cell;
		private ArrayList<Cell> neighbors;

		public Neighbors() {
		}
		Neighbors(Cell cell) {
			this.cell = cell;
			neighbors = searchNeigbors();
		}

		// Getters and Setters

		public ArrayList<Cell> getNeighbors() {
			return neighbors;
		}

		ArrayList<Cell> searchNeigbors() {
			ArrayList<Cell> neighbors = new ArrayList<Cell>();
			int rowDirection[] = { -1, -1, -1, 0, 1, 1, 1, 0 };
			int columnDirection[] = { -1, 0, 1, 1, 1, 0, -1, -1 };
			int MAX_NEIGHBORS = 8;
			final int cellRow = cell.getRow();
			final int cellColumn = cell.getColumn();
			Cell temp = new Cell();
			for (int i = 0; i < MAX_NEIGHBORS; i++) {
				int nextRow = cellRow + rowDirection[i];
				int nextColumn = cellColumn + columnDirection[i];
				temp = table[nextRow][nextColumn];
				if (temp != null)
					neighbors.add(temp);
			}
			return neighbors;
		}
	}

	private class CellIterator implements Iterator<Table.Cell>, Serializable {
		
		private static final long serialVersionUID = 1L;
		private int column;
		private int row;
		boolean terminated;

		CellIterator() {
			row = 1;
			column = 1;
			terminated = false;
		}
		@Override
		public void remove() {
			
		}
		@Override
		public boolean hasNext() {
			if (terminated)
				return false;
			return true;
		}

		@Override
		public Cell next() {
			Cell next = table[row][column];
			if (row == SIZE && column == SIZE) {
				terminated = true;
			}
			if (column == SIZE) {
				column = 1;
				row++;
			} else
				column++;

			return next;
		}

	}

}
