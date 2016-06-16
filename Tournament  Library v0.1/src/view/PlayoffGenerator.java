package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ListView;
import javafx.scene.shape.Line;

public class PlayoffGenerator {
  ArrayList<String[]> results;
  static int counter = 0;
  private BufferedReader br;

  /**
   * Generate Playoff view
  * @param participators amount of participators
  * @return 
  */
  public Group generatePlayoffView(int participators) {

    participators = participators / 2;
    this.results = getResultsFromFile("C:/playoff.txt");

    Group root = new Group();
    double yOffset = 0;
    double xOffset = 0;
    for (int i = participators;i > 0;i = i / 2) {
      yOffset = yOffset + 50;
      Group round = generatePlayoffRound(i);
      round.setLayoutX(300 * xOffset++);
      round.setLayoutY(yOffset);
      root.getChildren().add(round);
    }
    return root;
  }

  /**
  * @param participators amount of participators
  * @return play of schema view
  */
  public Group generatePlayoffRound(int participators) {
    Group root = new Group();

    ArrayList<Group> connectorLines = new ArrayList<Group>();
    ArrayList<ListView<String>> lists = new ArrayList<ListView<String>>();
    for (int i = 0;i < participators; i++) {
      ObservableList<String> data = FXCollections.observableArrayList(
          results.get(counter)[0] + "\t\t"
          + results.get(counter)[1], 
          results.get(counter)[2] + "\t\t"  
          + results.get(counter)[3]);

      ListView<String> tmp = new ListView<String>(data);
      if (Integer.parseInt(results.get(counter)[1]) > Integer.parseInt(results.get(counter)[3])) {
        tmp.getSelectionModel().select(0);
      } else {
        tmp.getSelectionModel().select(1);
      }

      tmp.setMaxSize(200, 60);
      tmp.setLayoutY(i * 100);
      lists.add(tmp);

      Group group = setConnector(i,tmp.getLayoutX(),tmp.getLayoutY());
      connectorLines.add(group);
      counter++;
    }
    if (connectorLines.size() == 1) {
      connectorLines.remove(0);
    }

    root.getChildren().addAll(connectorLines);
    root.getChildren().addAll(lists);
    return root;
  }

  /**
   * Gets Playoff data from file
  * @param path path to file
  * @return array with file rows
  */
  public ArrayList<String[]> getResultsFromFile(String path) {
    String thisLine;
    ArrayList<String[]> results = new ArrayList<String[]>();
    //Open the file for reading
    try {
      br = new BufferedReader(new FileReader(path));
      while ((thisLine = br.readLine()) != null) { // while loop begins here
        results.add(thisLine.split(","));
      }
    } catch (IOException e) {
      System.err.println("Error: " + e);
    }
    return results;
  }

  /**
  * @param i
  * @param boxX current game box x position
  * @param boxY current game box y position
  * @return group of lines what conects boxes
  */
  public Group setConnector(int i, double boxX, double boxY) {
    Group root = new Group();
    Line line = new Line();
    Line line2 = new Line();

    line.setStartX(200.0f);
    line.setStartY(boxY + 30.0f);
    line.setEndX(220.0f);
    line.setEndY(boxY + 30.0f);
    root.getChildren().add(line);

    if (i % 2 == 0) {
      line2.setStartX(220.0f);
      line2.setStartY(boxY + 30.0f);
      line2.setEndX(220.0f);
      line2.setEndY(boxY + 130f);

      root.getChildren().add(line2);
    }

    return root;
  }
}
