package wwwordz;

import java.util.stream.Stream;

public class TestData {
	
	protected static final String NICK     = "fulano";
	protected static final String PASSWORD = "****";
	
	protected static final String OTHER_NICK = "beltrano";
	protected static final String OTHER_PASSWORD = "+++";
	
	protected static Stream<String> nameProvider() {
		return Stream.of(
				null,
				"",
				"Fulano", 
				"Beltrano", 
				"Sicrano",
				"Fulano de Tal", 
				"Beltrano de Qual", 
				"Sicraninho de Tal");
	}
}
