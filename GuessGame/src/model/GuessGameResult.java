package model;

import model.Result;
import strategies.Strategy;

public class GuessGameResult implements Result {

  private Strategy strategy;
  private int score = 3;

  /**
  * @param strategy current Strategy
  */
  public GuessGameResult(Strategy strategy) {
    this.strategy = strategy;
  }
  
  /**
  * @return Strategy score
  */
  public Integer getScore() {
    return score;
  }

  /**
   * Sets strategy score
  * @param score strategy score
  */
  public void setScore(Integer score) {
    this.score = score;
  }

  /**
  * @return link to the strategy object
  */
  public Strategy getStrategy() {
    return strategy;
  }

  /**
   * Sets link to the strategy object
  * @param strategy current strategy
  */
  public void setStrategy(Strategy strategy) {
    this.strategy = strategy;
  }

  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    return strategy + ": " + score;
  }
}
