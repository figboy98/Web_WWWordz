package wwwordz.puzzle;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wwwordz.server.Generator;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table;
import wwwordz.shared.TableTest;

public class GeneratorTest {
	Generator generator;
	Table table;
	
	public static int REPETITIONS = 1000;
	public static int MINIMUM_GENERATED_SOLUTIONS = 30;
	public static int MINIMUM_RANDOM_SOLUTIONS = 10;
			
	
	@BeforeEach
	public void before() {
		generator = new Generator();
	}

	/**
	 * Test repeated puzzle generation
	 * 1) all different puzzles
	 * 2) reasonable number of solutions 
	 */
	@Test
	public void testGenerate() {
		
		Set<Table> tables = new HashSet<>();
		
		int sum = 0, count = 0;
		for(int i=0; i<REPETITIONS; i++) {
			Puzzle puzzle= generator.generate();
			assertTrue(tables.add(puzzle.getTable()),"This table was already generated");
			
			sum += puzzle.getSolutions().size();
			count++;
		}
		assertTrue((sum/count) >= MINIMUM_GENERATED_SOLUTIONS );
	}

	/**
	 * Check random puzzles and number of solutions
	 */
	@Test
	public void testRandom() {
		
		int sum = 0, count = 0;
		for(int i=0; i<REPETITIONS; i++) {
			Puzzle puzzle = generator.random();
			
			sum += puzzle.getSolutions().size();
			count++;
		}
		assertTrue((sum/count) >= MINIMUM_RANDOM_SOLUTIONS );
	}
	
	/**
	 * Expected words in solution
	 */
	String[] words = {
			"PATO",	"POLACO","ATOL","COBOL","COPADO","OBTIDA","LOBITO","LATOADA"
			,"BOLOTADA","PADIOLA","APITO","API", "DIPOLO" };
	
	/**
	 * Check solutions of given table
	 */
	@Test
	public void solutions() {
		
		assertEquals(0,		generator.getSolutions(new Table()).size(),
				"Empty table should have no solutions");
		
		List<Solution> solutions = generator.getSolutions(new Table(TableTest.data));
		assertTrue(solutions.size() > 150,"This table has more than 150 solution");
		
		for(String word: words)
			assertTrue(contains(solutions,word),"Word "+word+" expected in solutions");
	}
	
	
	/**
	 * Check if given list of solutions already contains a word
	 * @param solutions
	 * @param word
	 * @return
	 */
	private boolean contains(List<Solution> solutions, String word) {
		for(Solution solution:solutions)
			if(solution.getWord().equals(word))
				return true;
		return false;
	}

}
