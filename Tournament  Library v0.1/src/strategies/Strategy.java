package strategies;

import controller.Game;

/**
 * @author Aleksandr
 *
 */
public interface Strategy {
   /**
   * @param game
   */
  public void nextMove(Game game);
}
