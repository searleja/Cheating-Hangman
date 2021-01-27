package hangman;

public class GuessAlreadyMadeException extends Exception {
  public GuessAlreadyMadeException(String s) {
    super(s);
  }
}
