package strategies;

import controller.Game;
import controller.GuessGame;

public class FourFiveSevenStrategy implements Strategy {
  int counter = 3;

  /* (non-Javadoc)
  * @see strategies.Strategy#nextMove(controller.Game)
  */
  @Override
  public void nextMove(Game game) {
    GuessGame guessGame = (GuessGame) game;
    guessGame.setPlayerAnswer(counter++);
  }
  
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
