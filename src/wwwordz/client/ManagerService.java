package wwwordz.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

@RemoteServiceRelativePath("manager")
public interface ManagerService extends RemoteService{
	public Puzzle getPuzzle() throws WWWordzException;
	public Collection<Rank> getRanking() throws WWWordzException;
	public long register(String nick, String password) throws WWWordzException;
	public void setPoints(String nick, int points) throws WWWordzException;
	public long timeToNextPlay();
	public boolean verify(String name, String password);
	
}
