package view;

import controller.TournamentController;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Result;
import strategies.Strategy;

public class GuessGameScene extends TournamentFXScene {

  /**
  * @param controller
  */
  public GuessGameScene(TournamentController controller) {
    super(controller);
  }

  /* (non-Javadoc)
  * @see view.TournamentFXScene#setResultsTable(controller.TournamentController)
  */
  @Override
  public TableView<Result> setResultsTable(TournamentController controller) {

    TableView<Result> result = new TableView<Result>();

    result.setPrefSize(200, 200);

    TableColumn<Result, String> strategyColumn = new TableColumn<Result, String>();
    strategyColumn.setPrefWidth(200);
    TableColumn<Result, Integer> scoreColumn = new TableColumn<Result, Integer>();

    strategyColumn.setCellValueFactory(new PropertyValueFactory<Result, String>("Strategy"));
    scoreColumn.setCellValueFactory(new PropertyValueFactory<Result, Integer>("Score"));

    strategyColumn.setText("Strategy");
    scoreColumn.setText("Score");

    result.getColumns().add(strategyColumn);
    result.getColumns().add(scoreColumn);

    controller.getResults().addListener(new MapChangeListener<Strategy,ObservableList<Result>>() {

      @Override
      public void onChanged(MapChangeListener.Change<? extends Strategy,
          ? extends ObservableList<Result>> change) {
        ObservableList<Result> items = FXCollections.observableArrayList();

        for (Strategy strategy : controller.getResults().keySet()) {
          items.addAll(controller.getResults().get(strategy));
          }

        result.setItems(items);
        }
      });
    
    return result;
    
  }

}
