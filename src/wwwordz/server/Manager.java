package wwwordz.server;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Manager  {
	private static ScheduledExecutorService worker=  Executors.newSingleThreadScheduledExecutor();
	private static Manager manager;
	private Round round = new Round();
	private static long	INITIAL_TIME = Round.getRoundDuration();

	private Manager() {
		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				round = new Round();
			}

		}, INITIAL_TIME, Round.getRoundDuration(), TimeUnit.SECONDS); 

	}


	public static Manager getInstance() {
		if(manager==null) {
			manager = new Manager();
		}
		return manager;
	}

	public Puzzle getPuzzle() throws WWWordzException {
		try {
			return round.getPuzzle();
		} catch(Exception e) {
			throw new WWWordzException("getPuzzle",e);
		}
	}

	public Collection<Rank> getRanking() throws WWWordzException {
		try {
			return round.getRanking();
		} catch(Exception e) {
			throw new WWWordzException("getRanking",e);
		}
	}

	public long register(String nick, String password) throws WWWordzException {
		try {
			return round.register(nick, password);
		} catch(Exception e) {
				throw new WWWordzException("Invalid User");
		}
	}

	public void setPoints(String nick, int points) throws WWWordzException {
		try{
			round.setPoints(nick, points);
		} catch(Exception e) {
			throw new WWWordzException("setPoints",e);
		}
	}
	public boolean verify(String name, String password) {
		return round.verify(name, password);
	}

	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}

}
