package wwwordz.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import wwwordz.shared.WWWordzException;

public class Players implements Serializable{

	private static final long serialVersionUID = 1L;
	private static File home = null;
	private static Players players = null;
	private static LinkedList<Player> playersList = null;
	

	private Players() {
		home = new File(System.getProperty("user.dir"));
		playersList = new LinkedList<Player>();
//		try {
//			restore();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}

	public void cleanup() {
		players = null;
		playersList = new LinkedList<Player>();
		try {
			backup();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static File getHome() {
		return home;
	}

	public static Players getInstance() {
		if(players == null)
			return players = new Players();
		return players;
	}

	public static void setHome(File home) {
		Players.home = home;
	}

	public boolean verify(String nick, String password) {
		Player player = getPlayer(nick);
		if(player == null ) {
			player = new Player(nick,password);
			playersList.add(player);
			return true;
		}
		if(player.getPassword().equals(password))
			return true;
		return false;
	}

	public Player getPlayer(String nick) {
		for(Player player: playersList) {
			if(player.getNick().equals(nick))
				return player;
		}
		return null;
	}

	public void addPoints(String nick, int points) throws WWWordzException {
		try {
			getPlayer(nick).setPoints(points);
			try {
				backup();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			throw new WWWordzException("addPoints",e);
		}
	}

	public void resetPoints(String nick) throws WWWordzException {
		try {
			getPlayer(nick).setPoints(0);
			try {
				backup();
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			throw new WWWordzException("resetPoints",e);
		}
	}

	public void backup() throws WWWordzException {
		try {
			String backupFile = home + "/backup.ser";
			FileOutputStream fileOutput = new FileOutputStream(backupFile);
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(playersList);
			objectOutput.close();
			fileOutput.close();
		}catch(Exception e) {
			throw new WWWordzException("backup",e);
		}
	}

//	public void restore() throws WWWordzException {
//		try {
//			String backupFile = home + "/backup.ser";
//			FileInputStream fileInput = new FileInputStream(backupFile);
//			ObjectInputStream objectIntput = new ObjectInputStream(fileInput);
//			@SuppressWarnings("unchecked")
//			LinkedList<Player> readObject = (LinkedList<Player>)objectIntput.readObject();
//			playersList = readObject;
//			objectIntput.close();
//			fileInput.close();
//		}catch(Exception e) {
//			throw new WWWordzException("restore",e);
//		}
//	}

}
