package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import controller.StrategyLoader;
import controller.StrategyLoaderFromPackage;
import controller.TournamentController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.ComputerPlayer;
import model.GameStats;
import model.Location;
import model.Player;
import model.PlayerStats;
import model.Result;
import strategies.Strategy;

/**
 * @author Aleksandr
 *
 */
public class GomokuTournamentController extends GomokuController implements TournamentController{
	private ObservableMap<Strategy,ObservableList<Result>> results = FXCollections.observableHashMap();
	private ObservableMap<Strategy,Exception> errors = FXCollections.observableHashMap();
	private DoubleProperty tournamentProgress = new SimpleDoubleProperty();
	
	public static Integer counter=0;
	protected ObservableList<PlayerStats> playersStats = FXCollections.observableArrayList();
	
	protected ObservableList<GameStats> gamesStats = FXCollections.observableArrayList();
	
	Strategy[] strategies = setStrategiesList();
	
	public GomokuTournamentController(GomokuGame game) {	
		super(game);
	}
	
	/**
	 * @return list of strategies
	 */
	public Strategy[] setStrategiesList(){
		
		return new StrategyLoaderFromPackage().getStrategyArray();
	}
	
	/**
	 * Starts a new tournament
	 * @param size of game board
	 * @param playerWName white player name
	 * @param playerBName black player name
	 * @throws IOException
	 */
	public void newTournament(int size, Strategy playerWName, Strategy playerBName) throws IOException {

		if (board != null) {
			board.getChildren().clear();
			board.setMinWidth(20 * size);
			board.setMinHeight(20 * size);
		}
		Player playerW = new ComputerPlayer(playerWName);
		Player playerB = new ComputerPlayer(playerBName);
		
		GomokuGame game = new GomokuGame(playerW, playerB, size, size);
		setGame(game);
		initialize();  
	}

	/**
	 * @param strategy strategy name
	 * @return Player
	 */
	public static Player getPlayerbyName(Strategy strategy) {
		Player player = new ComputerPlayer(strategy);
		return player;
	}
	
	/* (non-Javadoc)
	 * @see controller.GomokuController#initialize()
	 */
	protected void initialize() throws IOException {
		initializePlayerChange();
	}
	
	/* (non-Javadoc)
	 * @see controller.GomokuController#initializePlayerChange()
	 */
	protected void initializePlayerChange() {
		game.currentPlayerProperty().addListener((event) ->
		{
			Player player = game.currentPlayerProperty().get();
			player.getSquareState();
			if (player instanceof ComputerPlayer) {

				game.resetMoveStartTime();
				((ComputerPlayer)player).getMove(game);
				Location l = game.getPlayerAnswer();
				game.makeMove(player, l);
			}
		});
		// handler is defined, let's start the game

		game.start();

	}
	
	/* (non-Javadoc)
	 * @see controller.TournamentController#getErros()
	 */
	@Override
	public ObservableMap<Strategy, Exception> getErros() {
		return errors;
	}
	/* (non-Javadoc)
	 * @see controller.TournamentController#getResults()
	 */
	@Override
	public ObservableMap<Strategy, ObservableList<Result>> getResults() {
		return results;
	}
	/* (non-Javadoc)
	 * @see controller.TournamentController#getTournamentProgress()
	 */
	@Override
	public DoubleProperty getTournamentProgress() {
		return tournamentProgress;
	}
	/* (non-Javadoc)
	 * @see controller.TournamentController#startTournament(controller.StrategyLoader)
	 */
	@Override
	public void startTournament(StrategyLoader loader) {
			Strategy[] strategyList = loader.getStrategyArray();
			
			for(Integer i = 0; i < strategyList.length; i++){
				for(Integer j = 0; j <strategyList.length; j++){
				if(i != j){
					ObservableList<Result> firstStrategyResults = getResults().get(strategyList[j]);
					ObservableList<Result> secondStrategyResults = getResults().get(strategyList[i]);
					
					if(!getResults().containsKey(strategyList[j])){
						firstStrategyResults = FXCollections.observableArrayList();
					}
					if(!getResults().containsKey(strategyList[i])){
						secondStrategyResults = FXCollections.observableArrayList();
					}
					
					Result gameResult;
					try{	
						gameResult = game.play(strategyList[j],strategyList[i]);
					}catch(Exception e){
						getErros().put(strategyList[j], e);
						continue;
					}
							
					firstStrategyResults.add(gameResult);
					secondStrategyResults.add(gameResult);
					
					getResults().put(strategyList[j], firstStrategyResults);
					getResults().put(strategyList[i], secondStrategyResults);
					
					getTournamentProgress().set(new BigDecimal(Double.parseDouble(i.toString())/strategyList.length).setScale(2,RoundingMode.UP).doubleValue());
				}
				}
			}
			getTournamentProgress().setValue(1.0);
			
		}
	/**
	 *  Starts the tournament
	 */
	public void startTournament() {
		startTournament(new StrategyLoaderFromPackage());
	}
}
