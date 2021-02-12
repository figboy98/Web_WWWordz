package wwwordz.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

public class Round {
	private Date join;
	private Date play;
	private Date report;
	private Date ranking;
	private Date end;
	private Puzzle puzzle;
	private HashMap<String, Player> roundPlayers;
	private Generator generator;

	private Players players = Players.getInstance();
	private static long joinDuration = Configs.JOIN_STAGE_DURATION;
	private static long playDuration = Configs.PLAY_STAGE_DURATION;
	private static long reportDuration = Configs.REPORT_STAGE_DURATION;
	private static long rankingDuration = Configs.RANKING_STAGE_DURATION;
	private static long roundDuration;
	private LinkedList<Rank> roundRanking = new LinkedList<Rank>();

	private enum Stage {
		join, play, report, ranking
	};

	private enum Relative {
		before, after
	};

	public Round() {
		generator = new Generator();
		join = new Date();
		play = new Date(join.getTime() + joinDuration * 1000);
		report = new Date(play.getTime() + playDuration * 1000);
		ranking = new Date(report.getTime() + reportDuration * 1000);
		end = new Date(ranking.getTime() + rankingDuration * 1000);
		roundPlayers = new HashMap<String, Player>();
		puzzle = generator.generate();
		roundDuration = joinDuration + playDuration + reportDuration + rankingDuration;
	}

	public static void setJoinStageDuration(long stageDuration) {
		joinDuration = stageDuration;
	}

	public static void setRankingStageSuration(long stageDuration) {
		rankingDuration = stageDuration;
	}

	public static void setPlayStageDuration(long stageDuration) {
		playDuration = stageDuration;
	}

	public static void setReportStageDuration(long stageDuration) {
		reportDuration = stageDuration;
	}

	public static long getRoundDuration() {
		return roundDuration;
	}

	public long getTimetoNextPlay() {
		Date now = new Date();
		if (now.before(play))
			return play.getTime() - now.getTime();
		else
			return end.getTime() - now.getTime() + joinDuration;
	}

	public long register(String nick, String password) throws WWWordzException {
		Date now = new Date();
		if (now.after(play))
			raiseStageException(Relative.after, Stage.join);
		else if (!players.verify(nick, password))
			throw new WWWordzException("Invalid player");
		Player player = players.getPlayer(nick);
		roundPlayers.put(nick, player);
		return play.getTime() - now.getTime();
	}

	public Puzzle getPuzzle() throws WWWordzException {
		Date now = new Date();
		if (now.before(play)) {
			raiseStageException(Relative.before, Stage.play);
		} else if (now.after(report)) {
			raiseStageException(Relative.after, Stage.play);
		}

		return puzzle;
	}

	public void setPoints(String nick, int points) throws WWWordzException {
		Date now = new Date();
		if (now.before(report))
			raiseStageException(Relative.before, Stage.report);

		else if (now.after(ranking))
			raiseStageException(Relative.after, Stage.report);

		else if (!roundPlayers.containsKey(nick))
			throw new WWWordzException("Unknown player");

		roundPlayers.get(nick).setPoints(points);
	}

	public Collection<Rank> getRanking() throws WWWordzException {
		Date now = new Date();
		if (now.before(ranking))
			raiseStageException(Relative.before, Stage.ranking);
		if (roundRanking.size() == 0) {
			for (Player p : roundPlayers.values()) {
				Rank rank = new Rank(p.getNick(), p.getPoints(), p.getAccumulated());
				roundRanking.add(rank);
			}

			Collections.sort(roundRanking, new Comparator<Rank>() {
				@Override
				public int compare(Rank r1, Rank r2) {
					return r2.getPoints() - r1.getPoints();
				}
			});
		}

		return roundRanking;
	}

	public boolean verify(String name, String password) {
		return players.verify(name, password);
	}

	private void raiseStageException(Relative relative, Stage stage) throws WWWordzException {
		throw new WWWordzException("Not valid " + relative + " " + stage + " stage");
	}

}
