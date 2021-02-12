package wwwordz.game;


import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.shared.WWWordzException;

/**
* Template for a test class on Manager - YOU NEED TO IMPLEMENTS THESE TESTS!
* 
*/
@DisplayName("Manager")
public class ManagerTest extends TestData {

	
	/**
	 * Set stage durations in round before any tests
	 */
	@BeforeAll
	public static void prepare() {
		fail();
	}
	
	
	/**
	 * Get an instance for testing and wait for beginning of round
	 */
	@BeforeEach
	public void before()  throws  InterruptedException {
		fail();
	}
	
	
	/**
	 * Test values to start a next play stage
	 */
	@Test
	@DisplayName("Time to next play")
	public void testGetTimeToNextPlay() {
		fail();
	}
	

	/**
	 * Test a sequence of rounds, and for each one
	 * 1) register user and check time to start playing
	 * 3) get puzzle and check its a new one
	 * 4) add random points
	 * 5) get ranking and check expected accumulated points 
	 * @throws WWWordzException 
	 * @throws InterruptedException
	 */
	@Test
	@DisplayName("Rounds")
	public void TestRounds() throws InterruptedException, WWWordzException {
		fail();
	}
	
}
