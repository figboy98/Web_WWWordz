package wwwordz.client;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Configs;

public class PlayView extends Composite {
	private final ManagerServiceAsync ManagerService = GWT.create(ManagerService.class);
	private DockPanel playArea = new DockPanel();
	private HorizontalPanel countdownTimer = new HorizontalPanel();
	private VerticalPanel words = new VerticalPanel();
	private String currentWord = "";
	private PuzzleManager puzzle;
	private ASW_Trab3 mainPanel;
	private ArrayList<String> wordsList = new ArrayList<>();
	private Label remainingTime = new Label();
	private int MIL_TO_SECONDS = 1000;

	/**
	 * Constructor
	 * 
	 * @param mainPanel, copy of the main panel to have communication
	 */
	public PlayView(ASW_Trab3 main) {
		mainPanel = main;
		initWidget(playArea);
		stylePlayArea();

	}

	/**
	 * Start the play area for a new round
	 */
	public void initPlayArea() {
		puzzle.initPuzzle();
		initCountdownTimer(Configs.PLAY_STAGE_DURATION);
		removeDiscoveredWords();

	}

	/**
	 * Function so set the properties of the play area
	 */
	private void stylePlayArea() {
		playArea.setWidth(Window.getClientWidth() + "px");
//		playArea.setHeight(Window.getClientHeight() + "px");
		playArea.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		playArea.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		playArea.add(countdownTimer, DockPanel.NORTH);
		puzzle = new PuzzleManager();
		playArea.add(puzzle, DockPanel.CENTER);

		playArea.add(words, DockPanel.EAST);
		styleCountdownTimer();
		styleDiscoveredWords();

	}

	/**
	 * Set the properties of the Label word
	 * 
	 * @param word
	 */
	private void styleLabel(Label word) {
		int FONT_SIZE = 20;
		int PADDING = 5;
		word.getElement().getStyle().setFontSize(FONT_SIZE, Style.Unit.PT);
		word.getElement().getStyle().setPadding(PADDING, Style.Unit.PT);
	}

	/**
	 * Properties of the countdown timer
	 */
	private void styleCountdownTimer() {
		int FONT_SIZE = 100;
		int SPACING = 10;
		countdownTimer.setSpacing(SPACING);
		countdownTimer.add(remainingTime);
		remainingTime.getElement().getStyle().setFontSize(FONT_SIZE, Style.Unit.PT);

	}

	/**
	 * Set the properties of the discovered word panel
	 */
	private void styleDiscoveredWords() {
		int FONT_SIZE = 50;
		int PADDING_LEFT = 20;
		Label label = new Label("Palavras descobertas");
		label.getElement().getStyle().setFontSize(FONT_SIZE, Style.Unit.PT);
		words.add(label);
		words.getElement().getStyle().setPaddingLeft(PADDING_LEFT, Style.Unit.PT);

	}

	/**
	 * Adds the string to the discovered words panel
	 * 
	 * @param string
	 */
	private void addWords(String string) {
		Label word = new Label(string);
		styleLabel(word);
		words.add(word);
		wordsList.add(string);
		currentWord = "";
	}

	/**
	 * In the end of the round removes the discovered words to prepare the panel for
	 * the next round
	 */
	private void removeDiscoveredWords() {

		int size = words.getWidgetCount() - 1;
		for (int i = size; i > 0; i--) {
			Widget temp = words.getWidget(i);
			temp.removeFromParent();
		}

	}

	/**
	 * In the end of the play stage, report the points gained and prepare this panel
	 * for the next round
	 */
	private void reportPoints() {
		puzzle.reportPoints();
		clearPlay();
	}

	/**
	 * Start the countdown timer with the playTime
	 * 
	 * @param playTime
	 */
	private void initCountdownTimer(final long playTime) {
		int ONE_SECOND = 1000;
		remainingTime.setText(Long.toString(playTime));
		Timer timer = new Timer() {
			long aux = playTime;

			@Override
			public void run() {
				if (aux == 0) {
					remainingTime.setText(Long.toString(aux));
					this.cancel();
					reportPoints();

				}
				remainingTime.setText(Long.toString(aux));
				aux--;
			}

		};
		timer.scheduleRepeating(ONE_SECOND);

	}

	/**
	 * Reset the necessary parameters for the next round
	 */
	private void clearPlay() {
		wordsList.clear();
		currentWord = "";
		puzzle.enableRemainingButtons(false);
		puzzle.setNewWord(false);
		remainingTime.setText(Long.toString(Configs.PLAY_STAGE_DURATION));
	}

	/**
	 * Custom widget that creates and controls the puzzle
	 */
	private class PuzzleManager extends Composite {
		private int ROWS = 4;
		private int COLUMNS = 4;
		private DockPanel puzzlePanel = new DockPanel();
		private Grid grid = new Grid(ROWS, COLUMNS);
		private ArrayList<PuzzleButton> currentButtons = new ArrayList<PuzzleButton>();
		private Boolean newWord = true;
		private Puzzle puzzle;

		/**
		 * Constructor
		 */
		public PuzzleManager() {
			initWidget(puzzlePanel);
			puzzlePanel.add(grid, DockPanel.CENTER);

		}

		/**
		 * Setters
		 */
		public void setNewWord(boolean word) {
			newWord = word;
		}

		/**
		 * Function called to get the puzzle for the new round
		 */
		public void initPuzzle() {
			ManagerService.getPuzzle(new GetPuzzleCall());

		}

		/**
		 * Fill the grid with the puzzle
		 * 
		 * @param puzzle, the new puzzle received from the server
		 */

		private void initGrid(Puzzle puzzle) {
			this.puzzle = puzzle;

			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					char aux = puzzle.getTable().getLetter(i + 1, j + 1);
					String letter = String.valueOf(aux);
					PuzzleButton button = new PuzzleButton(letter);
					addHandlers(button);
					grid.setWidget(i, j, button);
				}
			}
			mapNeighbors();
		}

		/**
		 * Function to discover the neighbors of each button in the puzzle
		 */

		private void mapNeighbors() {
			int row = grid.getRowCount();
			int column = grid.getColumnCount();
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					PuzzleButton button = (PuzzleButton) grid.getWidget(i, j);
					ArrayList<PuzzleButton> neighbors = findNeighbors(i, j);
					button.addNeighbors(neighbors);
				}

			}
		}

		/**
		 * Verify the discovered words that are correct and calculate their points
		 * 
		 * @return the points gained in the round
		 */

		private int reportPoints() {
			int points = 0;

			for (String p : wordsList) {
				if (puzzle.containsWord(p)) {
					points += calculatePoints(p.length());

				}

			}
			ManagerService.setPoints(mainPanel.getNick(), points, new SetPointsCallback());
			return points;
		}

		/**
		 * Auxiliary function to calculate the points of each word
		 * 
		 * @param size, size of the word
		 * @return points of the word
		 */
		private int calculatePoints(int size) {
			if (size == 3)
				return 1;
			return 2 * calculatePoints(size - 1) + 1;
		}

		/**
		 * Add click and double click handlers to the button
		 * 
		 * @param button
		 */
		private void addHandlers(PuzzleButton button) {
			button.addDomHandler(new MyDoubleClickHanlder(), DoubleClickEvent.getType());
			button.addDomHandler(new MyClickHandler(), ClickEvent.getType());

		}

		/**
		 * Permanently block the buttons of finished words
		 */
		private void lockUsedButtons() {
			int size = currentButtons.size();
			PuzzleButton button;
			for (int i = 0; i < size; i++) {
				button = currentButtons.remove(0);
				button.setLockedButton();
			}
		}

		/**
		 * Activate/Deactivate the buttons that can still be used
		 * 
		 * @param disable, true for activate, false to deactivate
		 */
		private void enableRemainingButtons(boolean disable) {
			PuzzleButton button;
			boolean block = false;
			if (!disable)
				block = true;
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					button = (PuzzleButton) grid.getWidget(i, j);
					if (!button.getLocked()) {
						button.setTemporaryBlocked(block);
					}
				}

			}
		}

		/**
		 * Enable/Disable neighbors buttons of the button
		 * 
		 * @param button
		 * @param enable, true for enable neighbors, false to block neighbors
		 */
		private void enableNeighbors(PuzzleButton button, boolean enable) {
			boolean block = false;
			if (!enable)
				block = true;
			ArrayList<PuzzleButton> neighbors = button.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				PuzzleButton temp = (PuzzleButton) neighbors.get(i);
				if (!temp.getLocked())
					neighbors.get(i).setTemporaryBlocked(block);
			}
		}

		/**
		 * Function to find and add the neighbors of the button to the
		 * ArrayList<PuzzleButton> neighbors
		 * 
		 * @param r, row of the button
		 * @param c, column of the bottom
		 * @return the button neighbors
		 */
		private ArrayList<PuzzleButton> findNeighbors(int r, int c) {
			ArrayList<PuzzleButton> neighbors = new ArrayList<PuzzleButton>();
			int row = r - 1;
			int column = c - 1;
			int rowLimit = r + 1;
			int columnLimit = c + 1;
			if (row < 0)
				row = r;
			if (column < 0)
				column = c;
			if (rowLimit >= grid.getRowCount()) {
				rowLimit = r;
			}
			if (columnLimit >= grid.getColumnCount())
				columnLimit = c;

			for (int i = row; i <= rowLimit; i++) {
				for (int j = column; j <= columnLimit; j++) {
					PuzzleButton button = (PuzzleButton) grid.getWidget(i, j);
					if (i != r || j != c)
						neighbors.add(button);
				}
			}
			return neighbors;

		}

		/**
		 * 
		 * Async class that implements the get puzzle call to server
		 *
		 */
		private class GetPuzzleCall implements AsyncCallback<Puzzle> {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Puzzle result) {
				initGrid(result);
			}

		}

		/**
		 * 
		 * Async calls that implements the set points call to server
		 *
		 */

		private class SetPointsCallback implements AsyncCallback<Void> {
			long temp = Configs.REPORT_STAGE_DURATION;
			int wait = (int) temp * MIL_TO_SECONDS;
			int RANK = 2;

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Void result) {
				new Timer() {

					@Override
					public void run() {
						mainPanel.showPanel(RANK);

					}

				}.schedule(wait);

			}

		}

		/**
		 * 
		 * ClickHandler for the buttons of the puzzle
		 * 
		 */
		private class MyClickHandler implements ClickHandler {
			private PuzzleButton button = null;
			private PuzzleButton lastClickedButton = null;
			private int indexOfLastButton = 0;

			/**
			 * A new word is being selected
			 */
			private void startNewWord() {
				newWord = false;
				enableRemainingButtons(false);
				button.setTemporaryBlocked(false);
			}

			private void newClickedButton() {
				button.setClickedButton(true);
				currentButtons.add(button);

			}

			/**
			 * Block the button in the index, so it cannot be unselected, only the last
			 * clicked button can be unselected
			 * 
			 * @param index
			 */

			private void disableNeighborsLastClickedButton(int index) {
				lastClickedButton = currentButtons.get(index);
				if (lastClickedButton != null)
					enableNeighbors(lastClickedButton, false);
			}

			/**
			 * The last button clicked is being unclicked It's necessary to: ->Put the
			 * button in a unclicked state ->Remove the letter and the button of the button
			 * from the current word and from the current Buttons list ->Enable the
			 * neighbors of the last clicked button ->Start a new word if there is no
			 * clicked button
			 */
			private void unclickButton() {
				button.setClickedButton(false);
				enableNeighbors(button, false);
				indexOfLastButton = currentButtons.size() - 1;
				currentButtons.remove(indexOfLastButton);
				currentWord = currentWord.substring(0, currentWord.length() - 1);
				indexOfLastButton--;

				if (indexOfLastButton >= 0) {
					lastClickedButton = currentButtons.get(indexOfLastButton);
					enableNeighbors(lastClickedButton, true);
					lastClickedButton.setTemporaryBlocked(false);
					disableCurrentButtons();
				} else {
					newWord = true;
					enableRemainingButtons(true);
				}

			}

			private void disableCurrentButtons() {
				int size = currentButtons.size();
				for (int i = 0; i < size - 1; i++) {
					currentButtons.get(i).setTemporaryBlocked(true);
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				button = (PuzzleButton) event.getSource();

				if (newWord) {
					startNewWord();

				}
				// If the button is blocked do nothing
				if (button.getTemporaryBlocked())
					return;

				else if (!button.getClicked()) {
					newClickedButton();
					indexOfLastButton = currentButtons.size() - 2;

					if (indexOfLastButton >= 0) {
						disableNeighborsLastClickedButton(indexOfLastButton);
						currentButtons.get(indexOfLastButton).setTemporaryBlocked(true);

					}

					enableNeighbors(button, true);

					button.setTemporaryBlocked(false);
					/*
					 * Disable all current buttons but the last one added,
					 */
					disableCurrentButtons();

					currentWord += button.getElement().getInnerText();

				}
				/**
				 * Last clicked button is being unclicked
				 */
				else if (button.getClicked()) {
					unclickButton();

				}

				/**
				 * Last clicked button is being unclicked
				 */
				else if (button.getClicked()) {
					unclickButton();

				}

			}
		}

		/**
		 * DoubleClickHandler for the buttons of the puzzle
		 */
		private class MyDoubleClickHanlder implements DoubleClickHandler {
			int MIN_WORD_SIZE = 3;

			private void wordFinished() {
				lockUsedButtons();
				addWords(currentWord);
				/**
				 * Prepares the puzzle for a new word
				 */
				newWord = true;
				enableRemainingButtons(true);

			}

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				PuzzleButton button = (PuzzleButton) event.getSource();
				// Buttons blocked don't have any action
				if (button.getTemporaryBlocked()) {
					return;
				}
				/**
				 * The button being clicked is the final letter of the word and wasn't clicked
				 * yet
				 */
				if (!button.getClicked()) {
					currentWord += button.getElement().getInnerText();
					currentButtons.add(button);

				}

				if (currentWord.length() < MIN_WORD_SIZE) {
					Window.alert("Minimo de 3 letras");
					/**
					 * Remove the letter added to the current word. Has to be done because the first
					 * clicked of the double click activates the single click handler that adds the
					 * letter
					 */
					currentWord = currentWord.substring(0, currentWord.length() - 1);
					return;
				}
				wordFinished();

			}

		}

		/**
		 * 
		 * Class that implements each button of the puzzle
		 * 
		 */

		private class PuzzleButton extends Composite {
			private Button button;
			private Boolean clicked = false;
			private Boolean temporaryBlocked = false;
			private Boolean locked = false;
			private String LIGHT_GREEN = "#66ff33";
			private String LIGHT_GREY = "#b3b3b3";
			private ArrayList<PuzzleButton> neighbors;

			/**
			 * Constructor
			 */
			PuzzleButton(String letter) {
				HorizontalPanel panel = new HorizontalPanel();
				button = new Button(letter);
				stylePuzzleButton();
				panel.add(button);
				neighbors = new ArrayList<PuzzleButton>();
				initWidget(panel);

			}

			/**
			 * Getters and Setters
			 */
			public void setLockedButton() {
				locked = true;
				button.getElement().getStyle().setBackgroundColor(LIGHT_GREY);
				button.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
				button.setEnabled(false);
			}

			/**
			 * @param isClicked, true to change the button to the clicked style. False to
			 *                   return to the default style
			 */
			public void setClickedButton(boolean isClicked) {
				if (isClicked) {
					button.getElement().getStyle().setBackgroundColor(LIGHT_GREEN);
					clicked = true;
				} else {
					clicked = false;
					stylePuzzleButton();
				}
			}

			/**
			 * Function to enable/disable the blocked use when a button is inserted/removed
			 * from a unfinished word
			 * 
			 * @param blocked, true to enable the block, false to disable
			 */
			private void setTemporaryBlocked(boolean blocked) {
				if (blocked) {
					temporaryBlocked = true;
					button.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
				} else {
					temporaryBlocked = false;
					button.getElement().getStyle().setCursor(Style.Cursor.POINTER);

				}
			}

			/**
			 * getters
			 */

			public boolean getClicked() {
				return clicked;
			}

			public boolean getLocked() {
				return locked;
			}

			public boolean getTemporaryBlocked() {
				return temporaryBlocked;
			}

			public ArrayList<PuzzleButton> getNeighbors() {
				return neighbors;
			}

			public void addNeighbors(ArrayList<PuzzleButton> neighbors) {
				this.neighbors = neighbors;
			}

			/**
			 * Default style of the button
			 */
			public void stylePuzzleButton() {
				int BORDER_WIDTH = 2;
				int HEIGHT = 100;
				int WIDTH = 100;
				int FONT_SIZE = 50;
				button.removeStyleName("gwt-Button");
				button.getElement().getStyle().setProperty("outline", "none");
				button.getElement().getStyle().setBackgroundColor("transparent");
				button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
				button.getElement().getStyle().setBorderWidth(BORDER_WIDTH, Style.Unit.PT);
				button.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
				button.getElement().getStyle().setHeight(HEIGHT, Style.Unit.PT);
				button.getElement().getStyle().setWidth(WIDTH, Style.Unit.PT);
				button.getElement().getStyle().setFontSize(FONT_SIZE, Style.Unit.PT);

			}

		}

	}

}
