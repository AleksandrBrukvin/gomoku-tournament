package controller;

import strategies.Strategy;

/**
 * @author Aleksandr
 *
 */
public interface StrategyLoader {
  /**
  * @return Array with strategies
  */
  public Strategy[] getStrategyArray();
}
