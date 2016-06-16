package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.GuessGame;
import controller.RoundRobinTournamentController;
import controller.StrategyLoaderFromPackage;
import model.GuessGameResult;
import strategies.GuessStrategy;

public class GuessGameTests {

  GuessGame game = new GuessGame();

  @Test
  public void guessGameTests() throws Exception {
    StrategyLoaderFromPackage loader = new StrategyLoaderFromPackage();
    assertNotNull(loader);
    assertNotNull(loader.loadStrategy("GuessStrategy"));
    assertEquals(new GuessStrategy().getClass(), loader.loadStrategy("GuessStrategy").getClass());
    RoundRobinTournamentController controller = new RoundRobinTournamentController(game);
    assertNotNull(controller.getErros());
    assertNotNull(controller.getResults());
    assertNotNull(controller.getTournamentProgress());
    assertNotNull(controller.getGame());
    assertNotNull(game.play(new GuessStrategy()));
    GuessGameResult result = (GuessGameResult) game.play(new GuessStrategy());
    assertNotNull(result);
    assertNotNull(result.getScore());
    assertTrue(result.getScore() > -1 && result.getScore() < 4);
    assertNotNull(result.getStrategy());
  }

}
