package gomoku.tournament.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gomoku.Game;
import gomoku.tournament.model.GameStats;
import gomoku.tournament.model.PlayerStats;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class TournamentPlanMaker {
	boolean bigBoard= true;

	List<String> strategies;
	GomokuTournamentController controller;
	ArrayList<String[]> pairs = new ArrayList<String[]>();
	int counter=0;
	
	public TournamentPlanMaker(List<String> strategies,GomokuTournamentController controller){
		this.controller =controller;
		this.strategies =strategies;
		String[] pair = new String[2];
		for(String strategy:strategies){
			for(String strategy2:strategies){
				if(!strategy.equals(strategy2)){
					pair = new String[2];
					pair[0]=strategy;
					pair[1]=strategy2;
					pairs.add(pair);
					pairs.add(pair);
					System.out.println(pair[0]+" "+ pair[1]);
				}
			}
		}
	}
	
	public void start() throws IOException{
		counter=0;
		
		Game.PlayerScores.clearHistory();
		clearTables();
		
		controller.progressBar.setProgress(0);
		controller.percentage.setText("0%");
		
		String[] opponents = pairs.get(counter);
		controller.newTournament(10,opponents[0],opponents[1]);
	}
	
	public void next() throws IOException{
		counter++;
		int size;
		
		if(bigBoard){
			size=20;
			bigBoard=false;
		}else{
			size=10;
			bigBoard=true;
		}
		
		double percentage = counter*100/pairs.size()*0.01;		
		controller.progressBar.setProgress(percentage);
		controller.percentage.setText((int) (percentage*100)+"%");
		
		if(pairs.size()<=counter){
			System.out.println("Completed! Mängud kokku: "+counter);
			setUpTable();
			return;
		}
		
		String[] opponents =pairs.get(counter);
		System.out.println("Opponents: "+opponents[0]+" VS "+opponents[1]+" Size:"+size);
		
		controller.newTournament(size,opponents[0],opponents[1]);
	}
	
	private void setUpTable(){
		
		controller.playerColumn.setCellValueFactory(new PropertyValueFactory<PlayerStats, String>("name"));
		controller.winsColumn.setCellValueFactory(new PropertyValueFactory<PlayerStats, Integer>("wins"));
		controller.drawsColumn.setCellValueFactory(new PropertyValueFactory<PlayerStats, Integer>("draws"));
		controller.losesColumn.setCellValueFactory(new PropertyValueFactory<PlayerStats, Integer>("loses"));
		controller.winRateColumn.setCellValueFactory(new PropertyValueFactory<PlayerStats, Integer>("winRate"));
		
		controller.white.setCellValueFactory(new PropertyValueFactory<GameStats, String>("white"));
		controller.black.setCellValueFactory(new PropertyValueFactory<GameStats, String>("black"));
		controller.boardSize.setCellValueFactory(new PropertyValueFactory<GameStats, Integer>("boardSize"));
		controller.gameResult.setCellValueFactory(new PropertyValueFactory<GameStats, String>("gameResult"));
		
		for(String strategy:strategies){
			Game.PlayerScores.printPlayerScore(GomokuTournamentController.getPlayerbyName(strategy));
			controller.playersStats.add(Game.PlayerScores.getPlayerStats(strategy));
			
		}
		controller.playersTable.setItems(controller.playersStats);
		
		controller.playersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				controller.gamesTable.getItems().clear();
			    controller.gamesStats=Game.PlayerScores.getGameStats(newSelection.getStrategy());
			    controller.gamesTable.setItems(controller.gamesStats);
		    }
		});
	}
	private void clearTables(){
		controller.playersStats.clear();
		controller.playersTable.setItems(controller.playersStats);
		controller.gamesStats.clear();
	    controller.gamesTable.setItems(controller.gamesStats);
	}
}
