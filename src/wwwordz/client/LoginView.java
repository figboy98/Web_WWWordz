package wwwordz.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends Composite {
	private final ManagerServiceAsync ManagerService = GWT.create(ManagerService.class);
	private VerticalPanel topPanel = new VerticalPanel();
	private VerticalPanel loginPanel = new VerticalPanel();
	private boolean isLogged = false;
	private HorizontalPanel countdownTimer = new HorizontalPanel();
	private final TextBox nameInput = new TextBox();
	private final PasswordTextBox passwordInput = new PasswordTextBox();
	private final ASW_Trab3 mainPanel;

	/**
	 * Constructor
	 * 
	 * @param main, copy of main panel to have communication
	 */
	public LoginView(ASW_Trab3 main) {
		initWidget(topPanel);
		mainPanel = main;
		styleMainPanel();
	}

	/**
	 * Function to register player if is not logged in, and if is logged in starts
	 * the next round
	 */

	public void initLogin() {
		if (!isLogged) {
			logIn();
		} else {
			topPanel.clear();
			countdownTimer.clear();
			ManagerService.register(nameInput.getText(), passwordInput.getText(), new RegisterCallback());
		}

	}

	/**
	 * Used when the player is entering the game
	 */
	private void logIn() {
		makeLoginPanel();
	}

	/**
	 * Function to stylize Main Panel
	 */
	private void styleMainPanel() {
		topPanel.setWidth(Window.getClientWidth() + "px");
		topPanel.setHeight(Window.getClientHeight() + "px");
		topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}

	/**
	 * Function to stylize Login Panel
	 */
	private void styleLoginPanel() {
		String WIDTH_SIZE = "300px";
		String HEIGHT_SIZE = "300px";
		int PADDING_TOP = 50;
		loginPanel.setSize(WIDTH_SIZE, HEIGHT_SIZE);
		loginPanel.getElement().getStyle().setPaddingTop(PADDING_TOP, Style.Unit.PT);
		loginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	}

	/**
	 * Function to create the Login Panel
	 */
	private void makeLoginPanel() {
		styleLoginPanel();
		Label nameLabel = new Label("Nome");
		Label passwordLabel = new Label("Password");
		loginPanel.add(nameLabel);
		loginPanel.add(nameInput);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordInput);
		Button loginButton = new Button("Login");
		loginPanel.add(loginButton);
		loginButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ManagerService.register(nameInput.getText(), passwordInput.getText(), new RegisterCallback());
			}

		});
		topPanel.add(loginPanel);

	}

	/**
	 * Function to wait for the play stage of the game
	 * 
	 * @param time,   time needed to wait
	 * @param isSync, is player is in Sync with the game or if needs to wait
	 */

	public void waitForNextRound(final int time, final boolean isSync) {
		final int PLAY = 1;
		int MAIN_PANEL_SPACING = 20;
		int TEXT_SIZE = Window.getClientWidth() / 30;
		int ONE_SECOND = 1000;
		topPanel.add(countdownTimer);
		countdownTimer.setSpacing(MAIN_PANEL_SPACING);
		final Label nextRound;
		if (!isSync) {
			nextRound = new Label("Tempo para a próxima ronda");
		} else {
			nextRound = new Label("Jogo começa em ");
		}
		String wait = Long.toString(time) + " s";

		final Label waitTime = new Label(wait);

		countdownTimer.add(nextRound);
		countdownTimer.add(waitTime);
		nextRound.getElement().getStyle().setFontSize(TEXT_SIZE, Style.Unit.PX);
		waitTime.getElement().getStyle().setFontSize(TEXT_SIZE, Style.Unit.PX);

		Timer timer = new Timer() {
			long aux = time;

			@Override
			public void run() {
				if (aux == 0) {
					waitTime.setText(Long.toString(aux) + " s");
					this.cancel();
					if (!isSync) {
						countdownTimer.clear();
						ManagerService.register(nameInput.getText(), passwordInput.getText(), new RegisterCallback());
					} else
						mainPanel.showPanel(PLAY);
				}
				waitTime.setText(Long.toString(aux) + " s");
				aux--;
			}

		};
		timer.scheduleRepeating(ONE_SECOND);

	}

	/**
	 * 
	 * Class of AsyncCallback to register player in the game
	 *
	 */
	private class RegisterCallback implements AsyncCallback<Long> {
		@Override
		/**
		 * If not in the register stage of the game, verify if the login information is
		 * correct
		 */
		public void onFailure(Throwable caught) {
			topPanel.clear();
			ManagerService.verify(nameInput.getText(), passwordInput.getText(), new VerifyCallback());

		}

		@Override

		public void onSuccess(Long result) {
			int ONE_SECOND = 1000;
			mainPanel.setNick(nameInput.getText());
			long temp = result;
			int wait = (int) Math.ceil(temp / ONE_SECOND);
			isLogged = true;
			topPanel.clear();
			waitForNextRound(wait, true);
		}

	}

	/**
	 * 
	 * Class of AsyncCallback to get the remaining time until the next round starts
	 *
	 */
	private class NextPlayCallback implements AsyncCallback<Long> {

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(Long result) {
			long time = result;
			int wait = (int) Math.ceil(time / 1000);

			waitForNextRound(wait, false);
		}
	}

	/**
	 * 
	 * Class of AsyncCallback to verify if the login information is correct
	 *
	 */
	private class VerifyCallback implements AsyncCallback<Boolean> {

		@Override
		public void onFailure(Throwable caught) {

		}

		@Override
		public void onSuccess(Boolean result) {
			if (!result)
				Window.alert("Wrong password");
			else
				ManagerService.timeToNextPlay(new NextPlayCallback());

		}

	}
}
