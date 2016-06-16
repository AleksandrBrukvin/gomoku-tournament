package controller;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Result;
import strategies.Strategy;

public interface TournamentController {

  /**
   * Method what start tournament 
  * @param loader loader class instance what returns participating strategy list
  */
  public void startTournament(StrategyLoader loader);
  
 /**
  * @return tournament progress double value from 0.0 to 1.0
 */
  public DoubleProperty getTournamentProgress();
  
  /**
  * @return observable map with tournament results key is a strategy and 
  *     value is list with strategy results
  */
  public ObservableMap<Strategy,ObservableList<Result>> getResults();
  
  /**
  * @return array with errors from tournament results calculation
  */
  public ObservableMap<Strategy,Exception> getErros();
}
