package controller;

import java.util.Random;

import controller.Game;
import exceptions.IncorrectAnswerException;
import model.GuessGameResult;
import model.Result;
import strategies.Strategy;

public class GuessGame implements Game {
  final Random random = new Random();
  public static final int ATTEMPT_AMOUNT = 3;
  private int playerAnswer;

  /* (non-Javadoc)
  * @see controller.Game#play(strategies.Strategy[])
  */
  @Override
  public Result play(Strategy ... strategy) throws Exception {
 
    int numberToGuess = random.nextInt(10) + 1;

    Strategy currentStrategy = strategy[0];
 
    GuessGameResult result = new GuessGameResult(currentStrategy);

    for (int i = 0; i < ATTEMPT_AMOUNT; i++) {
      currentStrategy.nextMove(this);
      if (playerAnswer < 0 || playerAnswer > 10) {
        throw new IncorrectAnswerException();
      }

      if (numberToGuess != playerAnswer) {
        result.setScore(result.getScore() - 1);
      } else {
        break;
      }
    }

    return result;
  }

  /**
 * @param playerAnswer
 */
  public void setPlayerAnswer(int playerAnswer) {
    this.playerAnswer = playerAnswer;
  }

}
