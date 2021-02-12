package wwwordz.game;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.server.Player;
import wwwordz.server.Players;
import wwwordz.shared.WWWordzException;

@DisplayName("Players")
public class PlayersTest extends TestData {

	private static final int REPEAT = 10;

	Players players;
	
	
	@BeforeEach
	/**
	 * Make default users for tests and cleanup
	 */
	public void setup() {
		players = Players.getInstance();
		
		players.cleanup();
	}
	
	@AfterEach
	/**
	 * Make sure existing users are always discarded before new tests
	 */
	public void cleanup() {
		players.cleanup();
	}
	
	@Nested
	@DisplayName("User Home")
	class UserHomeTest {
		/**
		 * Home exists and is a file
		 */
		@Test
		@DisplayName("Home is a File")
		public void testGetHome() {
			assertAll("getHome",
					() -> assertNotNull(Players.getHome(),
							"Some object expected as home"),
					() -> assertTrue(Players.getHome() instanceof File,
							"Home should be a File instance")
					);
		}

		/**
		 * Home reflects user.dir
		 */
		@Test
		@DisplayName("Home reflects user.dir")
		public void testSetHome() {
			File home = new File(System.getProperty("user.dir"));

			assertEquals(home,Players.getHome(),"Home dir expected");
		}
	}

	/**
	 * Check there is a single non null instance of Players
	 */
	@Test
	@DisplayName("Is singleton")
	public void testGetInstance() {
		Players players = Players.getInstance();
		assertAll("getInstace",
				() -> assertNotNull(players,"Some object expected as Players"),
				() -> assertTrue(players == Players.getInstance(),
						"Same instance")
				);
	}

	/**
	 * Verify a player with a password:
	 * 1) first time it should be created
	 * 2) second time it already exists (verified)
	 * 3) with a different password it must fail
	 * 4) repeat this for many users (with same prefix)
	 */
	@Test
	@DisplayName("Verify")
	public void testVerify() {
		
		assertAll("verify",
			() ->
			assertTrue(players.verify(NICK,PASSWORD),"Player should be created"),
			() -> 
			assertTrue(players.verify(NICK,PASSWORD),"Player should exist"),
			() ->
			assertTrue(	! players.verify(NICK,OTHER_PASSWORD) ,
				"Player should already exist with different password"),
			() -> {
		
				for(int i=0; i< REPEAT; i++)
					assertTrue(players.verify(NICK+i,PASSWORD),
							"Player should be created");
		
				for(int i=0; i< REPEAT; i++)
					assertTrue(	! players.verify(NICK+i,OTHER_PASSWORD), 
							"Player should already exist with different password");
	
				for(int i=0; i< REPEAT; i++)
					assertTrue(players.verify(NICK+i,PASSWORD),
							"Player should exist");
			});

	}

	/**
	 * Test if can get a previously created player with
	 * 1) nick
	 * 2) password
	 * 3) points (0)
	 * 4) accumulated points (p)
	 */
	@Test
	public void testGetPlayer() {
		assertTrue(players.verify(NICK,PASSWORD),"Player should be created");
		
		Player player = players.getPlayer(NICK);
		assertAll("Player",
				() -> assertNotNull(player,"Player should be non null"),
				() -> assertEquals(NICK,player.getNick(),"nick of player should be "+NICK),
				() -> assertEquals(PASSWORD,player.getPassword(),"password of player should be "+PASSWORD),
				() -> assertEquals(0,player.getPoints(),"points of player should be 0"),
				() -> assertEquals(0,player.getAccumulated(),"accumulated of player should be 0")
				);
	
		assertNull(players.getPlayer(OTHER_NICK),"Player should be  null");
	}
	
	@Nested
	@DisplayName("Points")
	class PlayerPointsTest {
		/**
		 * Check if points are altered after added
		 * @throws WWWordzException 
		 */
		@Test
		@DisplayName("Add points")
		public void testAddPoints() throws WWWordzException {

			assertTrue(players.verify(NICK,PASSWORD),"Player should be created");
			int points = 100;

			players.addPoints(NICK, points);

			Player player = players.getPlayer(NICK);
			assertEquals(points,player.getPoints(),"Wrong points");

			assertThrows(WWWordzException.class, 
					() -> players.addPoints(OTHER_NICK, points));

		}

		/**
		 * Test is point are zero after reseted
		 * @throws WWWordzException 
		 */
		@Test
		@DisplayName("Reset points")
		public void testResetPoints() throws WWWordzException {

			assertTrue(players.verify(NICK,PASSWORD),"Player should be created");
			int points = 100;

			players.addPoints(NICK, points);

			players.resetPoints(NICK);

			Player player = players.getPlayer(NICK);
			assertEquals(0,player.getPoints(),"Wrong points");

			assertThrows(WWWordzException.class,
					() -> players.resetPoints(OTHER_NICK) );
		}
	}

}
