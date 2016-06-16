package model;

import model.Result;

public class GameStats implements Result {
	private String white;
	private String black;
	private Integer boardSize;
	private String gameResult;
	
	 /**
	 * @param white player name
	 * @param black player name
	 * @param boardSize size of board
	 * @param gameResult result of the game
	 */
	public GameStats(String white, String black, Integer boardSize,String gameResult) {
	        this.white = white;
	        this.black = black;
	        this.boardSize = boardSize;
	        this.setGameResult(gameResult);
	 }
	 
	/**
	 * @return white player name
	 */
	public String getWhite() {
		return white;
	}
	/**
	 * @param white player name
	 */
	public void setWhite(String white) {
		this.white = white;
	}
	/**
	 * @return black player name
	 */
	public String getBlack() {
		return black;
	}
	/**
	 * @param black sets black player name
	 */
	public void setBlack(String black) {
		this.black = black;
	}
	/**
	 * @return size of board
	 */
	public Integer getBoardSize() {
		return boardSize;
	}
	/**
	 * @param boardSize size of board
	 */
	public void setBoardSize(Integer boardSize) {
		this.boardSize = boardSize;
	}

	/**
	 * @return games results
	 */
	public String getGameResult() {
		return gameResult;
	}

	/**
	 * @param gameResult results of the game
	 */
	public void setGameResult(String gameResult) {
		this.gameResult = gameResult;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return white + " VS " + black + " Board size: " + boardSize + " Winner:" + gameResult;
		
	}
}
