package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        String dictionaryFileName = args[0];
        String inputLength = args[1];
        String inputGuesses = args[2];
        int wordLength = Integer.valueOf(inputLength);
        int numGuesses = Integer.valueOf(inputGuesses);
        File file = new File(dictionaryFileName);

        EvilHangmanGame game = new EvilHangmanGame();
        game.startGame(file, wordLength);

        Scanner guess = new Scanner(System.in);
        int counter = 0;
        while (counter < numGuesses) {
            char next = guess.next().charAt(0);
            game.makeGuess(next);
        }
    }

}
