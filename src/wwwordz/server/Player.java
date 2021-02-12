package wwwordz.server;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nick;
	private String password;
	private int points;
	private int accumulated;
	
	public Player() {}
	public Player(String nick, String password) {
		this.nick = nick;
		this.password = password;
		this.points = 0;
		this.accumulated = 0;
	}

	public String getNick() {
		return this.nick;
	}

	public String getPassword() {
		return this.password;
	}

	public int getPoints() {
		return this.points;
	}

	public int getAccumulated() {
		return this.accumulated;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPoints(int points) {
		this.points = points;
		this.accumulated += points;
	}

	public void setAccumulated(int accumulated) {
		this.accumulated =accumulated;	
	}


}
