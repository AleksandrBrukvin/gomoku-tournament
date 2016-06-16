package view;

import java.util.Map.Entry;

import controller.StrategyLoaderFromPackage;
import controller.TournamentController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import model.Result;
import strategies.Strategy;

public class ConsoleView {
  ObservableList<Result> tournamentResults = FXCollections.observableArrayList();

  /**
  * @param controller current Tournament controller instance
  */
  public ConsoleView(TournamentController controller) {
  
    controller.getTournamentProgress().addListener(new ChangeListener<Object>() {

      @Override
      public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
        printTournamentProgress(controller);
      }
    });

    controller.getResults().addListener(new MapChangeListener<Strategy,ObservableList<Result>>() {

    /* (non-Javadoc)
    * @see javafx.collections.
    *   MapChangeListener#onChanged(javafx.collections.MapChangeListener.Change)
    */
    @Override
    public void onChanged(MapChangeListener.Change<? extends Strategy,
        ? extends ObservableList<Result>> change) {
        saveTournamentResults(controller);
      }

    });

    controller.startTournament(new StrategyLoaderFromPackage());
  }

  public void printResults(ObservableList<Result> tournamentResults) {
    System.out.println("Results:" + tournamentResults);
  }

  /**
   * Prints tournament events into console
  * @param controller current Tournament controller instance
  */
  public void printTournamentProgress(TournamentController controller) {
    System.out.println("Tournament progress:" 
        + (controller.getTournamentProgress().getValue() * 100) + "%");;

    if (controller.getTournamentProgress().doubleValue() == 1.0) {
      for (Strategy strategy : controller.getResults().keySet()) {
        controller.getResults().get(strategy).clear();
      }

      for (Entry<Strategy, Exception> entry : controller.getErros().entrySet()) {
        System.out.println("Warning: Stratgy" + entry.getKey() + " caused " 
            + entry.getValue() + ". Strategy was removed from the tournament!");
      }
      System.out.println("Tournament is finish!\n");

      printResults(tournamentResults);
    }

  }

  /** 
   * Adds results into class observable list variable
  * @param controller current Tournament controller instance
  */
  public void saveTournamentResults(TournamentController controller) {
    ObservableList<Result> results = FXCollections.observableArrayList();

    for (Strategy strategy : controller.getResults().keySet()) {
      results.addAll(controller.getResults().get(strategy));
    }
    tournamentResults.clear();
    tournamentResults.addAll(results);

  }
}
