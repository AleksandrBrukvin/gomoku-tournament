package model;

import java.util.ArrayList;

import model.Result;

public class PlayerStats implements Result{

    private String name;
    private String strategy;
    private int wins;
    private int draws;
    private int loses;
    private int winRate;
    private ArrayList<String> gamesLog;
 
    public PlayerStats(int place, String name, int wins, int winRate) {
        this.name = name;
        this.wins = wins;
        this.winRate = winRate;
    }
    
    public PlayerStats(String name, String strategy, int wins, int draws, int loses, int winRate, ArrayList<String> gamesLog) {
        this.name = name;
        this.setStrategy(strategy);
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
        this.winRate = winRate;
        this.gamesLog = gamesLog;
    }
 

    public PlayerStats() {
    }
 
 
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
 
    /**
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
 
    /**
     * @return draws amount
     */
    public int getDraws() {
        return draws;
    }
 
    /**
     * @param draws amount
     */
    public void setDraws(int draws) {
        this.draws = draws;
    }
    
    /**
     * @return loses amount
     */
    public int getLoses() {
        return loses;
    }
 
    /**
     * @param loses amount
     */
    public void setLoses(int loses) {
        this.loses = loses;
    }
    
    /**
     * @return wins amount
     */
    public int getWins() {
        return wins;
    }
 
    /**
     * @param wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }
 
    /**
     * @return winrate
     */
    public int getWinRate() {
        return winRate;
    }
 
    /**
     * @param winRate
     */
    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }

	/**
	 * @return strategy
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return game log list
	 */
	public ArrayList<String> getGamesLog() {
		return gamesLog;
	}
	
}