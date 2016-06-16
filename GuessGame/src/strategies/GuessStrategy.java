package strategies;

import java.util.Random;

import controller.Game;
import controller.GuessGame;

public class GuessStrategy implements Strategy {
  Random random = new Random();

  /* (non-Javadoc)
  * @see strategies.Strategy#nextMove(controller.Game)
  */
  @Override
  public void nextMove(Game game) {
    GuessGame guessGame = (GuessGame) game;
    guessGame.setPlayerAnswer(random.nextInt(10) + 1);

  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
