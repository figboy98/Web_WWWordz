package wwwordz.shared;

import java.io.Serializable;

public class Rank implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nick;
	private int points;
	private int accumulated;

	public Rank() {
	}

	public Rank(String nick, int points, int accumulated) {
		this.nick = nick;
		this.accumulated = accumulated;
		this.points = points;
	}
 
	public Integer getPoints() {
		return points;
	}

	public String getNick() {
		return nick;
	}

	public Integer getAccumulated() {
		return accumulated;
	}

	public void setPoints(int value) {
		points = value;

	}

	public void setNick(String nick) {
		this.nick = nick;

	}

	public void setAccumulated(int value) {
		this.accumulated = value;
	}

}
