package main;

import controller.GuessGame;
import controller.RoundRobinTournamentController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GuessGameScene;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    RoundRobinTournamentController controller = new RoundRobinTournamentController(new GuessGame());
    //new ConsoleView(controller);

    GuessGameScene view = new GuessGameScene(controller);

    primaryStage.setScene(view.getScene());
    primaryStage.show();
  }

}
