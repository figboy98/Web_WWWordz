package wwwordz.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import wwwordz.TestData;

public class RankTest extends TestData {

	Rank rank;
	
	String nick = "fulano";
	int points = 100;
	int accumulated = 5000;
	
	@BeforeEach
	public void before() {
		rank = new Rank(nick,points,accumulated);
	}
	
	@Test
	@DisplayName("Check if rank is null")
	public void testRank() {
		assertNotNull(rank);
	}

	
	@ParameterizedTest
	@MethodSource("nameProvider")
	@DisplayName("Test nick setter & getter")
	public void testANick(String nick) {
		rank.setNick(nick);
		assertEquals(nick,rank.getNick(),"Wrong nick");
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 10, 100, 1000 })
	@DisplayName("Test points setter & getter")
	public void testPoints(int value) {
		rank.setPoints(value);
		assertEquals(value,rank.getPoints(),"Wrong points");
	}
	


	@ParameterizedTest
	@DisplayName("Test getAccumulated")
	@ValueSource(ints = { 10, 100, 1000 })
	public void testGetAccumulated(int value) {
		rank.setAccumulated(value);
		assertEquals(value,rank.getAccumulated());
	}

}
