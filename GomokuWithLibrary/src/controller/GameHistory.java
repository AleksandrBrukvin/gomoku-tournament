package controller;

import model.Location;
import model.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.GomokuGame.GameStatus;
import controller.GomokuGame.SquareState;

/**
 * Handles game history
 * @author Ago
 *
 */
public class GameHistory {
  static class GameHistoryEntry {
    /**
    * Format to be used in log file.
    */
    public static final String FORMAT = "%s %2d,%2d %s %2.4f\n";
    public static final int DURATION_NULL = -1;
    //private Player player;
    private SquareState squareState;
    private LocalDateTime start;

    /**
    * Duration of the move. -1 ==> not set.
    */
    private long duration = DURATION_NULL;
    private Location move;

    public GameHistoryEntry(SquareState squareState, Location move,
        LocalDateTime start, long duration) {

      this.squareState = squareState;
      this.move = move;
      this.start = start;
      this.duration = duration;
    }

    /**
     * @param duration duration of move
     */
    public void setDuration(double duration) {
      this.duration = (long) (duration * 1_000_000_000);
    }

    /**
     *  sets duration based on current time and start time
     */
    public void end() {
      if (duration == DURATION_NULL) {
        // let's set duration based on current time and start time
        duration = Duration.between(start, LocalDateTime.now()).toNanos();
      }
    }
    
    /**
     * @return duration
     */
    public long getDuration() {
      return duration;
    }

    /**
     * @return move
     */
    public Location getMove() {
      return move;
    }
  }

  private List<GameHistoryEntry> entries = new ArrayList<GameHistoryEntry>();

  private Player playerWhite;
  private String playerWhiteName;
  private Player playerBlack;
  private String playerBlackName;
  private LocalDateTime gameStart;
  private int boardSize;

  /**
  * The result of the game.
  */
  private GameStatus gameStatus = GameStatus.OPEN;

  /**
  * Loads game history from File object.
  * TODO: should do some input check.
    @param file File object
  * @return Game history instance
  * @throws IOException In case there is a problem reading file
  */
  public static GameHistory load(File file) throws IOException {
    // let's change the locale for numbers@HB%$! 
    Locale.setDefault(Locale.US);
    GameHistory gh = new GameHistory();
    List<String> lines = Files.readAllLines(file.toPath());
    // start: xxx
    Pattern startPattern = Pattern.compile("start:\\s(.*)");
    Matcher startMatcher = startPattern.matcher(lines.get(0));
    if (startMatcher.find()) {
      String startTime = startMatcher.group(1);
      gh.gameStart = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    // board: 10
    String[] tokens = lines.get(1).split(" ");
    if (tokens.length == 2) {
      gh.boardSize = Integer.parseInt(tokens[1]);
    }
    // player white name
    Pattern playerPattern = Pattern.compile(".\\s(.*)");
    Matcher playerMatcher = playerPattern.matcher(lines.get(2));
    if (playerMatcher.find()) {
      gh.playerWhiteName = playerMatcher.group(1);
    }
    // player black name
    playerMatcher = playerPattern.matcher(lines.get(3));
    if (playerMatcher.find()) {
      gh.playerWhiteName = playerMatcher.group(1);
    }
    // moves
    Pattern movePattern = Pattern.compile("(.)\\s+(\\d+),\\s*(\\d+)\\s+([^ ]+)\\s+([\\d.]*)");
    for (int i = 3; i < lines.size() - 1; i++) {
      String line = lines.get(i);
      Matcher moveMatcher = movePattern.matcher(line);
      if (moveMatcher.find()) {
        SquareState squareState = moveMatcher.group(1).equals("B") 
            ? SquareState.BLACK : SquareState.WHITE;
        int row = Integer.parseInt(moveMatcher.group(2));
        int col = Integer.parseInt(moveMatcher.group(3));
        String dtStr = moveMatcher.group(4);
        double duration = Double.parseDouble(moveMatcher.group(5));
        LocalDateTime startTime = LocalDateTime.parse(dtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Location move = new Location(row, col);
        GameHistoryEntry e = new GameHistoryEntry(squareState, move, startTime, -1);
        e.setDuration(duration);
				gh.entries.add(e);
			}
		}
		return gh;
	}
	
	/**
	 * 
	 */
	private GameHistory() {
	}
	
	/**
	 * @param game gomoku game instance
	 */
	public GameHistory(GomokuGame game) {
		gameStart = LocalDateTime.now();
		playerWhite = game.getPlayerWhite();
		playerWhiteName = playerWhite.getName();
		playerBlack = game.getPlayerBlack();
		playerBlackName = playerBlack.getName();
		boardSize = game.getHeight();
	}
	
	/**
	 * @param player player what did a move
	 * @param move move location
	 */
	public void addMove(Player player, Location move) {
		LocalDateTime prev = null;
		if (entries.size() > 0) {
			// time from the last move
			GameHistoryEntry eLast = entries.get(entries.size() - 1);
			prev = eLast.start;
		} else {
			prev = gameStart;
		}
		LocalDateTime start = LocalDateTime.now();
		long duration = Duration.between(prev, start).toNanos();
		addMove(player, move, start, duration);
	}
	
	/**
	 * @param gameStatus game statuse object instance
	 */
	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	/**
	 * @param player player what did the move
	 * @param move current location of move
	 * @param start date then move was started
	 * @param duration duration of the move
	 */
	public void addMove(Player player, Location move, LocalDateTime start, long duration) {
		SquareState s = null;
		if (player == playerWhite) {
			s = SquareState.WHITE;
		} else if (player == playerBlack) {
			s = SquareState.BLACK;
		} else {
			throw new RuntimeException("player does not exist:" + player);
		}
		addMove(s, move, start, duration);
	}
	
	/**
	 * Ads a move to game history
	 * @param squareState current square state
	 * @param move current location of move
	 * @param start date then move was started
	 * @param duration duration of the move
	 */
	public void addMove(SquareState squareState, Location move, LocalDateTime start, long duration) {
		GameHistoryEntry e = new GameHistoryEntry(squareState, move, start, duration);
		entries.add(e);
	}
	
	
	/** saves results into the file
	 * @param filename  name of the file to save
	 */
	public void save(String filename) {
		// let's change the locale for numbers
		Locale.setDefault(Locale.US);
		StringBuilder out = new StringBuilder();
		out.append("start: " + gameStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n");
		out.append("board: " + boardSize + "\n");
		out.append("W " + playerWhiteName + "\n");
		out.append("B " + playerBlackName + "\n");
		for (GameHistoryEntry e : entries) {
			out.append(String.format(GameHistoryEntry.FORMAT,
					(e.squareState == SquareState.BLACK ? "B" : "W"),
					e.move.getRow(), e.move.getColumn(),
					e.start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					e.duration == -1 ? 0 : (float)e.duration / 1_000_000_000));
		}
		out.append("Result: " + gameStatus);
		
		try {
			Files.write(Paths.get(filename), out.toString().getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * @return list with game history entries
	 */
	public List<GameHistoryEntry> getEntries() {
		return entries;
	}

	/**
	 * @return white player name
	 */
	public String getPlayerWhiteName() {
		return playerWhiteName;
	}

	/**
	 * @return Black player name
	 */
	public String getPlayerBlackName() {
		return playerBlackName;
	}

	/**
	 * @return model of the status of the game
	 */
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	
	/**
	 * @return board size
	 */
	public int getBoardSize() {
		return boardSize;
	}


}