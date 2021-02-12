package wwwordz.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;

public interface ManagerServiceAsync{

	void getPuzzle(AsyncCallback<Puzzle> callback);

	void getRanking(AsyncCallback<Collection<Rank>> callback);

	void register(String nick, String password, AsyncCallback<Long> callback);

	void setPoints(String nick, int points, AsyncCallback<Void> callback);

	void timeToNextPlay(AsyncCallback<Long> callback);

	void verify(String name, String password, AsyncCallback<Boolean> callback);

}
