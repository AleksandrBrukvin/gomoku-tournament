package gomoku.tournament.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gomoku.ComputerPlayer;
import gomoku.ComputerStrategy;
import gomoku.ComputerStrategyOpponent;
import gomoku.Game;
import gomoku.Game.GameStatus;
import gomoku.tournament.model.GameStats;
import gomoku.tournament.model.PlayerStats;
import gomoku.GomokuController;
import gomoku.Main;
import gomoku.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GomokuTournamentController extends GomokuController{
	@FXML
	protected TableView<PlayerStats> playersTable;
 
    @FXML
    protected TableColumn<PlayerStats, String> playerColumn;
 
    @FXML
    protected TableColumn<PlayerStats, Integer> winsColumn;
    
    @FXML
    protected TableColumn<PlayerStats, Integer> drawsColumn;
    
    @FXML
    protected TableColumn<PlayerStats, Integer> losesColumn;
 
    @FXML
    protected TableColumn<PlayerStats, Integer> winRateColumn;
    
    @FXML
	protected TableView<GameStats> gamesTable;
 
    @FXML
    protected TableColumn<GameStats, String> white;
 
    @FXML
    protected TableColumn<GameStats, String> black;
    
    @FXML
    protected TableColumn<GameStats, Integer> boardSize;
    
    @FXML
    protected TableColumn<GameStats, String> gameResult;
    
	@FXML
    protected ProgressBar progressBar;
	
	@FXML
    protected Label percentage;
	
	protected ObservableList<PlayerStats> playersStats = FXCollections.observableArrayList();
	
	protected ObservableList<GameStats> gamesStats = FXCollections.observableArrayList();
	
	List<String> strategies = setStrategiesList();
	TournamentPlanMaker maker = new TournamentPlanMaker(strategies,this);
	
	public GomokuTournamentController(Game game) {	
		super(game);
	}
	public List<String> setStrategiesList(){
		List<String> strategies = Main.getStrategies();
		strategies.add("OpponentWeak");
		strategies.add("OpponentStrong");
		strategies.add("OpponentWinner2013");
		strategies.add("OpponentWinner2014");
		
		return strategies;
	}
	public void newTournament() throws IOException{
		maker.start();
		
  }

  public void newTournament(ArrayList<Player> tournamentParticipators) throws IOException {
    for (Player whitePlayer : tournamentParticipators) {
      for (Player blackPlayer : tournamentParticipators) {
        if (!whitePlayer.equals(blackPlayer)) {
          newGame(whitePlayer, blackPlayer, 10);
        }
      }
    }
  }

	public void newTournament(int size,String playerWName,String playerBName) throws IOException {

		if (board != null) {
			board.getChildren().clear();
			board.setMinWidth(20 * size);
			board.setMinHeight(20 * size);
		}
		Player playerW = getPlayerbyName(playerWName);
		Player playerB = getPlayerbyName(playerBName);
		
		Game game = new Game(playerW, playerB, size, size);
		setGame(game);
		initialize();  
	}

	public static Player getPlayerbyName(String strateegiaNimetus){
		Player player = null;
		if (strateegiaNimetus.equals("OpponentWeak")) {
			player = new ComputerPlayer(new ComputerStrategyOpponent(ComputerStrategyOpponent.WEAK));
		} else if (strateegiaNimetus.equals("OpponentStrong")) {
			player = new ComputerPlayer(new ComputerStrategyOpponent(ComputerStrategyOpponent.ADVANCED));
		} else if (strateegiaNimetus.equals("OpponentWinner2013")) {
			player = new ComputerPlayer(new ComputerStrategyOpponent(ComputerStrategyOpponent.WINNER));
		}  else {
				// tournament 2014/2015
				// let's load from gomoku.players
				ClassLoader classLoader = GomokuController.class.getClassLoader();
				try {
					Class<?> studentClass = classLoader.loadClass("gomoku.strategies." + strateegiaNimetus);
					Object s = studentClass.newInstance();
					ComputerStrategy cs = (ComputerStrategy)s;
					Player p = new ComputerPlayer(cs);
					player = p;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.out.println("Class " + strateegiaNimetus + " not found.");
					System.exit(1);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
		}
		return player;
	}
	
	protected void initialize() throws IOException {
		System.out.println("init");
		//setUpSquares();
		initializePlayerChange();
		initializeGameStatusListener();
	}
	
  protected void initializeGameStatusListener() {
    game.gameStatusProperty().addListener((event) -> {
      GameStatus status = game.gameStatusProperty().get();
      if (status == GameStatus.BLACK_WON || status == GameStatus.WHITE_WON || status == GameStatus.DRAW) {				
        try {
          maker.next();
        } catch (Exception exception) {
          exception.printStackTrace();
        }
        statusLabel.fontProperty().set(Font.font(null, FontWeight.BOLD, 12));
      }
      statusLabel.setText(status.toString());
    });
  }
}
