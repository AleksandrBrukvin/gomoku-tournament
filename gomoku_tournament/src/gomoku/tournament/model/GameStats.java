package gomoku.tournament.model;

public class GameStats {
	private String white;
	private String black;
	private Integer boardSize;
	private String gameResult;
	
	 public GameStats(String white, String black, Integer boardSize,String gameResult) {
	        this.white = white;
	        this.black = black;
	        this.boardSize = boardSize;
	        this.setGameResult(gameResult);
	 }
	 
	public String getWhite() {
		return white;
	}
	public void setWhite(String white) {
		this.white = white;
	}
	public String getBlack() {
		return black;
	}
	public void setBlack(String black) {
		this.black = black;
	}
	public Integer getBoardSize() {
		return boardSize;
	}
	public void setBoardSize(Integer boardSize) {
		this.boardSize = boardSize;
	}

	public String getGameResult() {
		return gameResult;
	}

	public void setGameResult(String gameResult) {
		this.gameResult = gameResult;
	}
}
