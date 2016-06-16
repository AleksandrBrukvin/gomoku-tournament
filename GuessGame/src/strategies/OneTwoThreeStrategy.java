package strategies;

import controller.Game;
import controller.GuessGame;

public class OneTwoThreeStrategy implements Strategy {
  int counter = 0;
  
  /* (non-Javadoc)
  * @see strategies.Strategy#nextMove(controller.Game)
  */
  @Override
  public void nextMove(Game game) {
    GuessGame guessGame = (GuessGame) game;
    guessGame.setPlayerAnswer(counter++);
  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
