package wwwordz.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

public class Dictionary {
	private final static Dictionary instance = new Dictionary();
	private static boolean isInicialized = false;
	private Trie dictionary = null;

	Dictionary() {
	}

	// Getters and Setters
	public static boolean isInicialized() {
		return isInicialized;
	}

	public static void setInicialized(boolean isInicialized) {
		Dictionary.isInicialized = isInicialized;
	}

	public Trie getDictionary() {
		return dictionary;
	}

	public void setDictionary(Trie dictionary) {
		this.dictionary = dictionary;
	}

	public static synchronized Dictionary getInstance() {
		if (isInicialized == true)
			return instance;
		else {
			instance.init();
			isInicialized = true;
			return instance;
		}
	}

	private void init() {

		if (dictionary == null)
			dictionary = new Trie();

		final String DIC_FILE = "wwwordz/server/pt-PT-AO.dic";

		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(DIC_FILE);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line;
			String arrStrings[];

			while ((line = reader.readLine()) != null) {
				arrStrings = line.split("/|\\s");
				line = arrStrings[0];

				line = Normalizer.normalize(line.toUpperCase(Locale.ENGLISH), Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				if (line.length() >= 3 && allLetters(line)) {
					dictionary.put(line);
				}
			}
			reader.close();
		} catch (IOException e) { System.out.println("Dictionary file not found");
		}

	}

	boolean allLetters(String line) {
		for (int i = 0; i < line.length(); i++) {
			if (!Character.isLetter(line.charAt(i)))
				return false;
		}
		return true;

	}

	public Trie.Search startSearch() {
		return dictionary.startSearch();

	}

	public String getRandomLargeWord() {

		return dictionary.getRandomLargeWord();
	}

}
