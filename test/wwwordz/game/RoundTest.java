package wwwordz.game;



import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.server.*;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

@DisplayName("Round")
public class RoundTest extends TestData {

	private static final int REPEAT = 100;
	
	static final long STAGE_DURATION = 100;
	static final long SLACK = 20;

	/**
	 * A round to test
	 */
	wwwordz.server.Round round;

	
	/**
	 * Set stage durations in round before any tests
	 */
	@BeforeAll
	public static void prepare() {
		Round.setJoinStageDuration(STAGE_DURATION);
		Round.setPlayStageDuration(STAGE_DURATION);
		Round.setReportStageDuration(STAGE_DURATION);
		Round.setRankingStageSuration(STAGE_DURATION);
	}
	
	/**
	 * Cleanup players before each test
	 */
	@BeforeEach
	public void cleanup() {
		Players.getInstance().cleanup();
		round = new Round();
	}
	
	/**
	 * Test round duration as equal of 4 stages
	 */
	@Test
	@DisplayName("Duration")
	public void getRoundDuration() {
		assertTrue(4*STAGE_DURATION == Round.getRoundDuration(),"Duration is 3 stages");
	}
	
	/**
	 * Test nextTimeToPlay
	 * 1) initial time less than preparation stage duration
	 * 2) increases consistently until start
	 * 3) after start play it must wait till next round
	 * @throws InterruptedException
	 */
	@Test
	@DisplayName("Time to next play")
	public void testGetTimeToNextPlay() throws InterruptedException {
		long time = round.getTimetoNextPlay();
		assertTrue(time<=STAGE_DURATION,"Less them stage duration");
  		Thread.sleep(time-SLACK);
		
		time = round.getTimetoNextPlay();
		assertTrue( time <= SLACK , "Just slack time before play");
		
		Thread.sleep(SLACK);
		
		assertTrue(round.getTimetoNextPlay() > Round.getRoundDuration() - SLACK,
				"Must wait till next round");
		
	}
	

	/**
	 * Test registration method.
	 * 1) multiple registrations with same data (OK)
	 * 2) multiple registrations with different password (exception)
	 * 3) no registration after game starts (exception)
	 * @throws InterruptedException
	 * @throws WWWordzException 
	 */
	@Test
	@DisplayName("Register")
	public void testRegister() throws InterruptedException, WWWordzException {
		
		assertAll("register",
				() -> {
					long time = round.register(NICK, PASSWORD);
			
					assertTrue(time>0,"Positive value expected");
			
					// register again: OK
					time = round.register(NICK, PASSWORD);
					assertTrue(time>0,"Positive value expected");
				},
				() -> {
		
					assertThrows(WWWordzException.class, 
							() -> round.register(NICK, OTHER_PASSWORD),	
							"register with a different password");
				},
				() -> {
		
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class, 
							() -> round.register(NICK, PASSWORD),
							"You can't register after game started");
				});	
			
	}	
	
	/**
	 * Test getPuzzle.
	 * 1) invocation before play stage: (exception)
	 * 2) invocation during play stage: OK
	 * 3) multiple invocations: the same puzzle
	 * 4) invocation after play stage: (exception)
	 * @throws InterruptedException
	 */
	@Test
	@DisplayName("Puzzle")
	public void testGetPuzzle() throws InterruptedException {
		
		assertAll("getPuzzle",
				() -> assertThrows(WWWordzException.class, 
						() -> round.getPuzzle(),
						"Exception expected before play stage"),	
				() -> {
				
					Thread.sleep(STAGE_DURATION);
		
					Puzzle puzzle = round.getPuzzle();
			
					assertNotNull(puzzle.getTable(),"Table expected in puzzle");
					assertNotNull(puzzle.getSolutions(),"Solutions expected in puzzle");
				},
				() -> {
		
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class,
							() -> round.getPuzzle(),
							"Exception expected after play stage");
				});
	}	
	
	
	/**
	 * Test setPoints method
	 * 1) Set points before report phase: (exception)
	 * 2) set points during report phase: OK
	 * 3) Set points after report phase: (exception)
	 * @throws InterruptedException
	 * @throws WWWordzException 
	 */
	@Test
	@DisplayName("Points")
	public void testSetPoints() throws InterruptedException, WWWordzException {
		
		assertAll("Set points at different stages",
				() -> {
					round.register(NICK, PASSWORD);
		
					assertThrows(WWWordzException.class, 
							() -> round.setPoints(NICK,0),
							"Exception expected in join stage");
				},
				() -> {
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class,
							() -> round.setPoints(NICK,0),
							"Exception expected in play stage");
				},
				() -> {
					Thread.sleep(STAGE_DURATION);

					round.setPoints(NICK,100);
				},
				() -> {
					Thread.sleep(STAGE_DURATION);
					
					assertThrows(WWWordzException.class, 
							() -> round.setPoints(NICK,0),
							"Exception expected in rankings stage");
				});
	}
	
	/**
	 * Test get rankings method
	 * 1) create many players and set points
	 * 2) attempt to get rankings before last phase: (exception) 
	 * 3) recover rankings with all players in correct order
	 * @throws InterruptedException
	 * @throws WWWordzException 
	 */
	@Test
	@DisplayName("Get Rankings")
	public void testGetRankings() throws InterruptedException, WWWordzException {
		
		assertAll("rankings",
				() -> {
					for(int i=0; i<REPEAT; i++)
						round.register(NICK+i, PASSWORD);
		
					assertThrows(WWWordzException.class, () ->round.getRanking(),
							"Exception expected in join stage");
				},
				() -> {		
					Thread.sleep(STAGE_DURATION);
		
					assertThrows(WWWordzException.class, () ->round.getRanking(),
							"Exception expected in play stage");
				},
				() -> {
					Thread.sleep(STAGE_DURATION);

					assertThrows(WWWordzException.class, () ->round.getRanking(),
							"Exception expected in report stage");
				},
				() -> {
					for(int i=0; i<REPEAT; i++)
						round.setPoints(NICK+i,i);
				
					Thread.sleep(STAGE_DURATION);
				},
				() -> {
					Collection<Rank>  ranking = round.getRanking();
					
					assertAll("-",
					() -> assertNotNull(ranking,"List of players expected"),
					
					() -> {
						Collection<Rank>  copy = round.getRanking();
						assertTrue(copy == ranking,"Exactly the same object expected");
					},
					() -> {
						int points = REPEAT;
						for(Rank player: ranking)
							assertEquals(--points,player.getPoints(),"Invalid points");
					});
				});

	}
}

