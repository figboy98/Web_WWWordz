package wwwordz.client;

import java.util.Collection;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;

public interface Manager {
	public Puzzle getPuzzle();
	public Collection<Rank> getRanking();
	public long register(String nick, String password);
	public void setPoints(String nick, int points);
	public long timeToNextPlay();
	public boolean verify(String nick, String password);

}
