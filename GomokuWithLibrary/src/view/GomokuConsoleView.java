package view;

import java.util.Map.Entry;

import controller.GomokuGame;
import controller.StrategyLoaderFromPackage;
import controller.TournamentController;
import model.PlayerStats;
import strategies.Strategy;

public class GomokuConsoleView extends ConsoleView{

	public GomokuConsoleView(TournamentController controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see view.ConsoleView#printTournamentProgress(controller.TournamentController)
	 */
	public void printTournamentProgress(TournamentController controller){
		System.out.println("\nTournament progress:" + 
				(controller.getTournamentProgress().getValue() * 100) + "%\n");
		
		if (controller.getTournamentProgress().doubleValue() == 1.0){
			for (Strategy strategy : controller.getResults().keySet()){
				controller.getResults().get(strategy).clear();
			}

			for (Entry<Strategy, Exception> entry : controller.getErros().entrySet()){
				System.out.println("Warning: Stratgy" + entry.getKey() + 
						" caused "+entry.getValue() + ". Strategy was removed from the tournament!");
			}
			System.out.println("Tournament is finish!\n");	
			Strategy[] strategys= new StrategyLoaderFromPackage().getStrategyArray();
			
			for (Strategy strategy : strategys) {
				PlayerStats stats = GomokuGame.PlayerScores.getPlayerStats(strategy);
				for (String gameLog : stats.getGamesLog()) {
					System.out.println(gameLog);
				}
				System.out.println("\nWins:" + stats.getWins() + " Draws:" +
						stats.getDraws() + " Loses:" + stats.getLoses()+
						" Winrate:" + stats.getWinRate() + "%\n");
			}
		}
		
	}
	
	
}
