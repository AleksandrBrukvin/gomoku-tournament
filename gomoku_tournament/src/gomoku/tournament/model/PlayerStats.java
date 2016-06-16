package gomoku.tournament.model;

public class PlayerStats {

    private String name;
    private String strategy;
    private int wins;
    private int draws;
    private int loses;
    private int winRate;
 
    public PlayerStats(int place, String name, int wins, int winRate) {
        this.name = name;
        this.wins = wins;
        this.winRate = winRate;
    }
    
    public PlayerStats(String name,String strategy, int wins,int draws,int loses, int winRate) {
        this.name = name;
        this.setStrategy(strategy);
        this.wins = wins;
        this.draws = draws;
        this.loses = loses;
        this.winRate = winRate;
    }
 
    public PlayerStats() {
    }
 
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public int getDraws() {
        return draws;
    }
 
    public void setDraws(int draws) {
        this.draws = draws;
    }
    
    public int getLoses() {
        return loses;
    }
 
    public void setLoses(int loses) {
        this.loses = loses;
    }
    
    public int getWins() {
        return wins;
    }
 
    public void setWins(int wins) {
        this.wins = wins;
    }
 
    public int getWinRate() {
        return winRate;
    }
 
    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
}
