package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import model.Result;
import strategies.Strategy;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;


public class RoundRobinTournamentController implements TournamentController {

  private Game game;
  private ObservableMap<Strategy,ObservableList<Result>> results = 
      FXCollections.observableHashMap();
  private ObservableMap<Strategy,Exception> errors = FXCollections.observableHashMap();

  private DoubleProperty tournamentProgress = new SimpleDoubleProperty();

  public RoundRobinTournamentController(Game game) {
    this.game = game;

  }

  /* (non-Javadoc)
 * @see controller.TournamentController#startTournament(controller.StrategyLoader)
 */
@Override
  public void startTournament(StrategyLoader loader) {
    Strategy[] strategyList = loader.getStrategyArray();

    for (Integer i = 0;i < strategyList.length; i++) {
      ObservableList<Result> strategyResults = FXCollections.observableArrayList();
      try {
        strategyResults.add(game.play(strategyList[i]));
      } catch (Exception exception) {
        errors.put(strategyList[i], exception);
        continue;
      }
      BigDecimal progress = new BigDecimal(Double.parseDouble(i.toString()) / strategyList.length);
      tournamentProgress.set(progress.setScale(2,RoundingMode.UP).doubleValue());
      results.put(strategyList[i], strategyResults);
    }
    tournamentProgress.setValue(1.0);
  }

  /* (non-Javadoc)
 * @see controller.TournamentController#getResults()
 */
@Override
  public ObservableMap<Strategy,ObservableList<Result>> getResults() {
    return results;
  }

  /**
  * @return 
  */
  public Game getGame() {
    return game;
  }

  /* (non-Javadoc)
  * @see controller.TournamentController#getTournamentProgress()
  */
  @Override
  public DoubleProperty getTournamentProgress() {
    return tournamentProgress;
  }

  /* (non-Javadoc)
  * @see controller.TournamentController#getErros()
  */
  @Override
  public ObservableMap<Strategy,Exception> getErros() {
    return errors;
  }
}
