package view;

import java.util.Map.Entry;

import controller.StrategyLoaderFromPackage;
import controller.TournamentController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Result;
import strategies.Strategy;

public class TournamentFxScene {
  private Scene scene;

  /**
  * @param controller current tournament controller instance
  */
  public TournamentFxScene(TournamentController controller) {
    VBox root = new VBox();
    HBox horizontalBox = new HBox();

    Button tournamentStart = setTournamentStartButton(controller);

    Group progressBarGroup = setTournamentProgressBarGroup(controller);

    TableView<Result> resultsTable = setResultsTable(controller);
    
    horizontalBox.getChildren().add(tournamentStart);
    horizontalBox.getChildren().add(progressBarGroup);
    root.getChildren().add(horizontalBox);
    root.getChildren().add(resultsTable);
    scene = new Scene(root);

  }
  
  public Scene getScene() {
    return scene;
  }

  /**
   * Sets progress bar and procentage label
  * @param controller current tournament controller instance
  * @return group what contains progress bar and procentage label
  */
  public Group setTournamentProgressBarGroup(TournamentController controller) {
    Group progressBarGroup = new Group();
    HBox horizontalBox = new HBox();

    ProgressBar bar = new ProgressBar();
    Label label = new Label();

    progressBarGroup.getChildren().add(horizontalBox);
    horizontalBox.getChildren().add(bar);
    horizontalBox.getChildren().add(label);

    bar.setPrefSize(200, 30);

    bar.setProgress(0.0);

    controller.getTournamentProgress().addListener(new ChangeListener<Object>() {

      @Override
      public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
        bar.setProgress(controller.getTournamentProgress().doubleValue());
        label.setText((controller.getTournamentProgress().getValue() * 100) + "%");

        if (controller.getTournamentProgress().doubleValue() == 1.0) {
          for (Strategy strategy : controller.getResults().keySet()) {
            controller.getResults().get(strategy).clear();
          }

          for (Entry<Strategy, Exception> entry : controller.getErros().entrySet()) {
            System.out.println("Warning: Stratgy" + entry.getKey() 
                + " caused " + entry.getValue() + ". Strategy was removed from the tournament!");
          }

          controller.getErros().clear();
          System.out.println("Tournament is finish!");
        }

        }
      });
    return progressBarGroup;

  }

  /**
   * Sets button what start tournament
  * @param controller current tournament controller instance
  * @return Button instance
  */
  public Button setTournamentStartButton(TournamentController controller){
    Button result = new Button("Start Tournament");

    result.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        controller.startTournament(new StrategyLoaderFromPackage());
      }
    });
    return result;

  }

  /**
  * @param controller current tournament controller instance
  * @return
  */
  public TableView<Result> setResultsTable(TournamentController controller) {

    TableView<Result> result = new TableView<Result>();
    return result;
  }
}
