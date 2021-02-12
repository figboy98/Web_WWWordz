package wwwordz.client;

import java.util.ArrayList;
import java.util.Collection;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import wwwordz.shared.Configs;
import wwwordz.shared.Rank;

public class RankingView extends Composite {
	private VerticalPanel panel = new VerticalPanel();
	private FlexTable rankTable = new FlexTable();
	private final ManagerServiceAsync ManagerService = GWT.create(ManagerService.class);
	private ASW_Trab3 mainPanel;

	public RankingView(ASW_Trab3 main) {
		initWidget(panel);
		mainPanel = main;
		Label ranking = new Label("Ranking");
		panel.add(ranking);
	}

	/**
	 * Function to start the ranking stage
	 */

	public void initRanking() {
		panel.clear();
		rankTable.clear();
		ManagerService.getRanking(new RankingCall());
		makeRankTable();

	}

	/**
	 * Function to make the rank table using a FlexTable
	 */

	private void makeRankTable() {
		rankTable.setText(0, 0, "Posição");
		rankTable.setText(0, 1, "Nome");
		rankTable.setText(0, 2, "Pontos última ronda");
		rankTable.setText(0, 3, "Pontos acumulados");
		StyleRankTable();
	}

	/**
	 * Function to stylize each row of the rank table
	 * 
	 * @param row, the row to be stylized
	 */

	private void StyleRowTable(int row) {
		for (int i = 0; i < 4; i++) {
			rankTable.getCellFormatter().setAlignment(row, i, HasHorizontalAlignment.ALIGN_CENTER,
					HasVerticalAlignment.ALIGN_TOP);
		}
	}

	/**
	 * Function to set the properties of the rank table
	 */
	private void StyleRankTable() {
		int BORDER_WIDTH = 1;
		int CELL_PADDING = 20;
		panel.setWidth(Window.getClientWidth() + "px");
		panel.setHeight(Window.getClientHeight() + "px");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		rankTable.setBorderWidth(BORDER_WIDTH);
		rankTable.setCellPadding(CELL_PADDING);
	}

	/**
	 * Function to display the information about the players of the round
	 * 
	 * @param rank, collection of the players registered in the round
	 */
	public void showRanking(Collection<Rank> rank) {
		ArrayList<Rank> ranking = new ArrayList<>(rank);

		int size = ranking.size();
		int position = 1;
		for (int i = 0; i < size; i++) {
			Rank player = ranking.get(i);
			addNewRow(player, position);

			position++;
		}
		panel.add(rankTable);

		waitForNextRound();

	}

	/**
	 * Function to add new rows to the rank table
	 * 
	 * @param rank, player information to be added
	 * @param row,  +index of the row where information will display
	 */
	private void addNewRow(Rank rank, int row) {
		int POSITION_COLUMN = 0;
		int NICK_COLUM = 1;
		int POINTS_COLUMN = 2;
		int ACC_POINTS_COLUMN = 3;
		rankTable.setText(row, POSITION_COLUMN, Integer.toString(row));
		rankTable.setText(row, NICK_COLUM, rank.getNick());
		rankTable.setText(row, POINTS_COLUMN, Integer.toString(rank.getPoints()));
		rankTable.setText(row, ACC_POINTS_COLUMN, Integer.toString(rank.getAccumulated()));
		StyleRowTable(row);

	}

	/**
	 * Function to wait the rank stage to finished and start a new round
	 */
	private void waitForNextRound() {
		final int LOGIN_PANEL = 0;
		long aux = Configs.RANKING_STAGE_DURATION;
		final int wait = (int) aux * 1000;
		new Timer() {

			@Override
			public void run() {
				mainPanel.showPanel(LOGIN_PANEL);
			}

		}.schedule(wait);
	}

	/**
	 * 
	 * class of AsyncCallback to get the ranking information
	 *
	 */

	private class RankingCall implements AsyncCallback<Collection<Rank>> {

		@Override
		public void onFailure(Throwable caught) {

		}

		@Override
		public void onSuccess(Collection<Rank> result) {
			showRanking(result);
		}

	}

}
