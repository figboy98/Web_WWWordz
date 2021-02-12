package wwwordz.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ASW_Trab3 implements EntryPoint {
	private VerticalPanel panel = new VerticalPanel();
	private DeckPanel deck = new DeckPanel();
	private Label gameName = new Label("WWWORDZ");
	private LoginView login;
	private PlayView play;
	private RankingView ranking;
	private String nick;
	int LOGIN = 0;
	int PLAY = 1;
	int RANKING = 2;

	/**
	 * Getters and setters
	 */
	public void setNick(String nick) {
		this.nick = nick;

	}

	public String getNick() {
		return nick;
	}
	/**
	 * Entry point
	 */
	public void onModuleLoad() {
		styleGameName();
		panel.add(gameName);
		initDeck();
		panel.add(deck);
		showPanel(LOGIN);
		RootPanel.get().add(panel);

	}
	
	private void styleGameName() {
		int FONT_SIZE=70;
		String BLUE="#00e6e6";
		panel.setWidth(Window.getClientWidth() + "px");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		gameName.getElement().getStyle().setFontSize(FONT_SIZE, Style.Unit.PT);
		gameName.getElement().getStyle().setColor(BLUE);

		
	}
	

	/**
	 * Creates and adds the panels to the deck
	 */
	private void initDeck() {

		login = new LoginView(this);
		play = new PlayView(this);
		ranking = new RankingView(this);
		deck.add(login);
		deck.add(play);
		deck.add(ranking);
	}
	/**
	 * Display the panel indicated in the index
	 * @param index
	 */
	public void showPanel(int index) {
		if (index == LOGIN) {
			login.initLogin();
		}
		if (index == PLAY) {
			play.initPlayArea();
		}
		if (index == RANKING) {
			ranking.initRanking();
		}
		deck.showWidget(index);
	}

}
