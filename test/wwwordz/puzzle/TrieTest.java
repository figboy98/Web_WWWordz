package wwwordz.puzzle;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.server.Trie;


public class TrieTest {

	static final List<String> WORDS = Arrays.asList(
			"a", "aa", "ab", "abc", 
			"b", "ba", "bac", "bc", 
			"ola", "ola mundo", "32");
	static final List<String> OTHERS = Arrays.asList(
			"c", "ac", "ad", "abd",
			"d", "bb", "bd", 
			"hello world", "pois", "asw" );
	
	
	/**
	 * Test putting many words
	 */
	@Test
	@DisplayName("Put many words")
	public void testPut() {
		Trie trie = new Trie();
		
		for(String word: WORDS)
			trie.put(word);
	}
	
	/**
	 * Test that trie is iterable - can be used in an extended loop
	 * to recover all its elements
	 */
	@Test
	@DisplayName("Iterable")
	public void testIterable() {
		Trie trie = new Trie();
		HashSet<String> expected = new HashSet<>(WORDS); 
		HashSet<String> obtained = new HashSet<>();
		
		for(String word: WORDS)
			trie.put(word);
		
		for(String name: trie) 
			obtained.add(name);
		
		assertEquals(expected,obtained);
		
	}
	
	
	/**
	 * Simple tests with 2 sets of words
	 */
	@Test
	public void testGetRandomLargeString() {
		
		checkWords(WORDS);
		checkWords(OTHERS);
	}
	
	private void checkWords(List<String> list) {
		Trie trie = new Trie();
		ArrayList<String> expected = new ArrayList<>();
		
		int sum=0, count=0;
		float expectedAverage, average;
		for(String word: list) {
			trie.put(word);
			expected.add(word);
			sum += word.length();
			count++;
		}
		expectedAverage = ((float) sum)/count;

		
		sum=0; count=0;
		for(int i=0; i< list.size() * 10 ; i++) {
			String random = trie.getRandomLargeWord();
			sum += random.length();
			count++;
			assertEquals(true,expected.contains(random),"strings must exist");
		}
		average= ((float) sum)/count;
		assertEquals(true, average  > expectedAverage,
				"string size must be larger than average");
	}
	
}
