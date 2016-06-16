package exceptions;

public class IncorrectAnswerException extends Exception {


  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
  * @see java.lang.Throwable#toString()
  */
  @Override
  public String toString() {
    return "IncorrectAnswerException: answer must be between 0 and 10";
  }
}
