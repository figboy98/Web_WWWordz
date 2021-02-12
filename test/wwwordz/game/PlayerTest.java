package wwwordz.game;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import wwwordz.TestData;
import wwwordz.server.Player;

@DisplayName("Player")
public class PlayerTest extends TestData {

	Player player;
	
	/**
	 * Create a player for testing
	 */
	@BeforeEach
	public void before() {
		player = new Player(NICK,PASSWORD);
	}
	
	/**
	 * Check if a non null player was created
	 */
	@Test
	@DisplayName("Player not null")
	public void testPlayer() {
		assertNotNull(player);
	}

	/**
	 * Check nicks
	 * 1) initial value
	 * 2) after changing it
	 */
	@ParameterizedTest
	@MethodSource("nameProvider")
	@DisplayName("Nick setter & getter")
	public void testNick(String nick) {
		player.setNick(nick);
		assertEquals(nick,player.getNick(),"Wrong nick");
	}

	/**
	 * Check passwords
	 * 1) original password
	 * 2) after changing it
	 */
	@ParameterizedTest
	@MethodSource("nameProvider")
	@DisplayName("Password setter & getter")
	public void testPassword(String password) {
		player.setPassword(password);
		assertEquals(password,player.getPassword(),"Wrong password");
	}

	/**
	 * Check points
	 * 1) initial value
	 * 2) after changing it
	 */
	@ParameterizedTest
	@DisplayName("Points")
	@ValueSource( ints = { 0, 10, 25, 50, 100 })
	public void testPoints(int points) {
		player.setPoints(points);
		assertEquals(points,player.getPoints(),"Wrong points");
	}

	/**
	 * Check accumulated points
	 * 1) initial value
	 * 2) after changing it
	 * 3) after setting points (should accumulate)
	 */
	@Test
	@DisplayName("Get Accumulated")
	public void testGetAccumulated() {
		
		assertAll("getAccumulated",
				() -> assertEquals(0,player.getAccumulated(),
						"0 if points undefined"),
				() -> {
					int accumulated = 100;
					player.setAccumulated(accumulated);
					assertEquals(accumulated,player.getAccumulated(),
							"Wrong accumulated ");
				},
				() -> {
					int points = 100;
					int accumulated = 100;
					
					player.setPoints(points);
					accumulated += points;
					assertEquals(accumulated,player.getAccumulated(),
							"Wrong accumulated ");
				});
	}

}
