package model;

import controller.Game;
import strategies.Strategy;

public class ComputerPlayer extends Player {
	/**
	 * Strategy object which is responsible
	 * of choosing the best move for the computer.
	 */
	private Strategy strategy;
	private Location playerAnswer;
	
	/**
	 * @param name strategy name
	 * @param strategy link
	 */
	public ComputerPlayer(String name, Strategy strategy) {
		super(name);
		this.strategy = strategy;
	}
	
	/**
	 * @param playerWName white player name
	 */
	public ComputerPlayer(Strategy playerWName) {
		super(playerWName.toString());
		this.strategy = playerWName;
	}
	
	/**
	 * @param game game instance
	 * @return
	 */
	public Location getMove(Game game) {
		this.strategy.nextMove(game);
		
		return playerAnswer;
	}

	/**
	 * @return player answer
	 */
	public Location getPlayerAnswer() {
		return playerAnswer;
	}

	/**
	 * @param playerAnswer answer of player move
	 */
	public void setPlayerAnswer(Location playerAnswer) {
		this.playerAnswer = playerAnswer;
	}
}
