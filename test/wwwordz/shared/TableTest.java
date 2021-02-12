package wwwordz.shared;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.shared.Table.Cell;

public class TableTest {
	Table table;
	
	@BeforeEach
	public void before() {
		table = new Table();
	}
	
	@Test
	@DisplayName("Table was created")
	public void instance() {
		assertNotNull(table,"instance expected");
	}

	@Test
	@DisplayName("Table has all expected cells")
	public void allCells() {
		int count = 0;
		for(Iterator<Cell> iterator = table.iterator(); 
				iterator.hasNext(); 
				iterator.next())
			count++;
			
		assertEquals(16,count,"Table should have 16 cells");
	}
	
	
	static final int[][] neigborCount = {
		{ 0,0,0,0,0,0 },
		{ 0,3,5,5,3,0 },
		{ 0,5,8,8,5,0 },
		{ 0,5,8,8,5,0 },
		{ 0,3,5,5,3,0 },
		{ 0,0,0,0,0,0 }
	};
	
	/**
	 * Check if number of numbers per cell is as expected
	 * Use static array of integers
	 */
	@Test
	@DisplayName("Number of neigbors in each cell")
	public void neigbors() {
		
		for(int line = 4; line > 0; line--)
			for(int column = 4; column>0; column--)
				assertEquals(neigborCount[line][column],
						table.getNeighbors(table.getCell(line,column)).size(),
						"Number of neigbors line "+line+" column "+ column+
						"should be "+neigborCount[line][column]);
	}

	
	public final static String[] data = {
			"PATO",
			"COLA",
			"BODA",
			"TIPO",
		};
	
	final static String[] data3 = {
			"PATA", // last letter different from data
			"COLA",
			"BODA",
			"TIPO",
		};
	
	/**
	 * Test table equality
	 */
	@Test
	@DisplayName("Table equality")
	public void testEquals() {
		Table table1 = new Table(data);
		
		assertAll("Table equality",
				() -> {
					
					Table table2 = new Table(data);
				
					assertEquals(table1,table2,"Tables with same data are equal");
				},
				() -> {
					
					Table table3 = new Table(data3);
		
					assertAll("different tables",
							() -> assertTrue(!table1.equals(table3)),
							() -> assertTrue(!table3.equals(table1))
							);
				}
			);		
	}
	
	
	
	
}
