package wwwordz.server;

import java.util.Collection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import wwwordz.client.ManagerService;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class ManagerServiceImpl extends RemoteServiceServlet implements ManagerService {
	
	private static final long serialVersionUID = 1L;
	private static  Manager manager = Manager.getInstance();
	
	 public ManagerServiceImpl() {
		manager= Manager.getInstance();
	}
	@Override
	public Puzzle getPuzzle() throws WWWordzException {
		return manager.getPuzzle();
	}

	@Override
	public Collection<Rank> getRanking() throws WWWordzException {
		return manager.getRanking();
	}

	@Override
	public long register(String nick, String password) throws WWWordzException {
		return manager.register(nick, password);
		
	}

	@Override
	public void setPoints(String nick, int points) throws WWWordzException {

		 manager.setPoints(nick, points);
	}

	@Override
	public long timeToNextPlay() {
		return manager.timeToNextPlay();
	}
	@Override
	public boolean verify(String name, String password) {
		return manager.verify(name, password);
	}


}
