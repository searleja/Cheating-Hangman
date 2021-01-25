package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    ArrayList<String> allWords;
    SortedSet<Character> guesses;
    Set<String> newDictionary;
    Map<Integer, Set<String>> currentWords;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        allWords = new ArrayList<String>();
        Scanner scan = new Scanner(dictionary);
        while (scan.hasNextLine()) {
            String current = scan.nextLine();
            if (current.length() == wordLength) { //add to the game
                allWords.add(current);
            }
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (!guesses.contains(guess)) { //will put into guessexception
            guesses.add(guess);
        }


        return newDictionary;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guesses;
    }
}
