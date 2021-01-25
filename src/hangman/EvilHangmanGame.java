package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangmanGame implements IEvilHangmanGame {

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
