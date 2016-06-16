import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.PlayoffGenerator;


public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  /* (non-Javadoc)
  * @see javafx.application.Application#start(javafx.stage.Stage)
  */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Test");
    primaryStage.setScene(new Scene(new PlayoffGenerator().generatePlayoffView(8)));
    primaryStage.show();
  }

}
