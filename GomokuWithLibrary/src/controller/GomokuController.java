package controller;

import java.io.File;
import java.io.IOException;

import controller.GameHistory.GameHistoryEntry;
import controller.GomokuGame.GameStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.ComputerPlayer;
import model.Location;
import model.Player;
import model.Square;
import strategies.Strategy;

public class GomokuController {
	
	protected GomokuGame game;
	private Square[][] squares;
	
	// white player
	@FXML
	protected ChoiceBox<String> playerWChoiceBox;
	
	// black player
	@FXML
	protected ChoiceBox<String> playerBChoiceBox;
	
	public GomokuController(GomokuGame game, int numberOfGames, boolean switchSides, boolean bothSizes) {
		this(game);
		game.getPlayerWhite();
		game.getPlayerBlack();
	}
	
	public GomokuController(GomokuGame game) {
		setGame(game);
	}

	@FXML
	protected GridPane board;
	
	@FXML
	protected Label statusLabel;
	
	@FXML
	private Label playerLabel;
	
	@FXML
	private HBox replayHBox;
	
	@FXML
	protected Button saveReplay;
	
	/**
	 * Sets up the game (initializes squares array).
	 * @param game
	 */
	public void setGame(GomokuGame game) {

		this.game = game;
		squares = new Square[game.getHeight()][game.getWidth()];
	}
	
	protected void initialize() throws IOException {
		setUpSquares();
		initializePlayerChange();
		initializeGameStatusListener();
		
		
	}
	
	
	/**
	 *  Initialize GameStatus listener
	 */
	protected void initializeGameStatusListener() {
		game.gameStatusProperty().addListener((event) -> 
		{
			
			GameStatus status = game.gameStatusProperty().get();
			saveReplay.visibleProperty().set(false);
			if (status == GameStatus.BLACK_WON || status == GameStatus.WHITE_WON) {
				statusLabel.fontProperty().set(Font.font(null, FontWeight.BOLD, 12));
				if (!game.inReplayMode().get()) {
					// let's show "save replay" only if not in replay mode already
					saveReplay.visibleProperty().set(true);
				}
			}
			statusLabel.setText(status.toString());
		});
	}
	/**
	 * Initializes playerChage listener
	 * @throws IOException 
	 */
	protected void initializePlayerChange() throws IOException {
		game.currentPlayerProperty().addListener((event) ->
		{
			Player player = game.currentPlayerProperty().get();
			playerLabel.setText(player.getSquareState() + " (" + player.getName() + ") to move");
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
	
	/**
	 * Sets up squares
	 * @throws IOException
	 */
	protected void setUpSquares() throws IOException {
		int squareSize = Square.SIZE_10;
		if (game.getWidth() == 20) {
			squareSize = Square.SIZE_20;
		}
		if (board != null && board.getScene() != null) {
			board.getScene().getWindow().setWidth(game.getWidth() * squareSize + 22);
			board.getScene().getWindow().setHeight(game.getHeight() * squareSize + 115);
		}
		board.getChildren().clear();
		for (int row = 0; row < game.getHeight(); row++) {
			for (int col = 0; col < game.getWidth(); col++) {
				final Square square = new Square(row, col, game, squareSize);
				board.getChildren().add(square);
				squares[row][col] = square;
			}
		}
	}
	/**
	 * Sets board grid pane
	 * @param boardGridPane
	 */
	public void setBoardGridPane(GridPane boardGridPane) {
		this.board = boardGridPane;
	}
	
	/**
	 * Sets fxml buttons functions
	 * @throws IOException
	 */
	@FXML
	public void newGame20() throws IOException {
		newGame(20);
	}
	/**
	 * @param white white player
	 * @param black black player
	 * @param i
	 * @throws IOException
	 */
	@FXML
	public void newGame(Player white, Player black, int i) throws IOException {
		newGame(10);
	}
	

	/**
	 * @param size of game board
	 * @throws IOException
	 */
	public void newGame(int size) throws IOException {
		if (board != null) {
			board.getChildren().clear();
			board.setMinWidth(20 * size);
			board.setMinHeight(20 * size);
		}
		String[] playerNames = new String[2];
		playerNames[0] = (String) this.playerWChoiceBox.getSelectionModel().getSelectedItem();
		playerNames[1] = (String) this.playerBChoiceBox.getSelectionModel().getSelectedItem();
		Player[] players = new Player[2];
		
		for (int i = 0; i < 2; i++) {
			if (playerNames[i].equals("Human")) {
			} else {
				// let's load from gomoku.players
				ClassLoader classLoader = GomokuController.class.getClassLoader();
				try {
					Class<?> studentClass = classLoader.loadClass("gomoku.strategies." + playerNames[i]);
					Object s = studentClass.newInstance();

					Strategy cs = (Strategy)s;
					Player p = new ComputerPlayer(cs);
					players[i] = p;
				} catch (ClassNotFoundException e) {
					System.exit(1);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		Player playerW = players[0];
		Player playerB = players[1];
		
		GomokuGame game = new GomokuGame(playerW, playerB, size, size);
		setGame(game);
		initialize();

		// change current player to start event handler
		// this is done when handler is set up
		//game.start();
	}
// History 
	private GameHistory gameHistory = null;
	/**
	 * Next move will be with the given index,
	 * previous will be -1
	 */
	private int gameHistoryIndex = 0;
	
	/**
	 * Loads the replay
	 */
	public void loadReplay() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open replay file");
		File file = fileChooser.showOpenDialog(board.getScene().getWindow());
		if (file != null) {
			try {
				gameHistory = GameHistory.load(file);
				GomokuGame game = new GomokuGame(new Player(gameHistory.getPlayerWhiteName() + " (Replay white)"), 
						new Player(gameHistory.getPlayerBlackName() + " (Replay black)"), 
						gameHistory.getBoardSize(), gameHistory.getBoardSize());
				game.setInReplayMode(true);
				setGame(game);
				initialize();
				replayHBox.visibleProperty().set(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * next move function for fxml
	 */
	@FXML
	public void nextMove() {
		try {
			GameHistoryEntry e = gameHistory.getEntries().get(gameHistoryIndex);
			game.makeMove(e.getMove());
			gameHistoryIndex++;
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	/**
	 *  Takes back all the moves
	 */
	public void firstMove() {
		// let's take back all the moves
		while (gameHistoryIndex > 0) previousMove();
	}
	
	/**
	 *  Takes back previous move
	 */
	public void previousMove() {
		try {
			if (gameHistoryIndex <= 0) {
				throw new IndexOutOfBoundsException();
			}
			gameHistoryIndex--;
			GameHistoryEntry e = gameHistory.getEntries().get(gameHistoryIndex);
			game.undoMove(e.getMove());
		} catch (IndexOutOfBoundsException e) {
		}
		
	}
	/**
	 * Shows next move
	 */
	public void play() {
		playNextMove();
	}
	/**
	 * Shows next move
	 */
	void playNextMove() {
		nextMove();
		if (gameHistoryIndex < gameHistory.getEntries().size()) {
			GameHistoryEntry e = gameHistory.getEntries().get(gameHistoryIndex);
			Timeline tl = new Timeline(new KeyFrame(Duration.millis(e.getDuration() / 1_000_000.0), ev -> playNextMove()));
			tl.setCycleCount(1);
			tl.play();
		}
	}
	
	/**
	 * Shows the last move
	 */
	public void lastMove() {
		while (gameHistoryIndex < gameHistory.getEntries().size()) nextMove();
	}
	
	/**
	 * Saves replay to the file
	 */
	public void saveReplay() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save replay file");
		File file = fileChooser.showSaveDialog(board.getScene().getWindow());
		if (file != null) {
			game.writeHistoryLog(file.getPath());
		}
	}
}
