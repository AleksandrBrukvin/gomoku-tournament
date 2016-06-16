package controller;

import model.Result;
import strategies.Strategy;

public interface Game {

  /**
   * Method what calculates game results of Strategies putted as arguments
   * @param strategy Strategy varrargs list
   * @return Resutl interface implemented model with game results
   * @throws Exception 
   */
  public Result play(Strategy ... strategy) throws Exception;

}
