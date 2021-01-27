package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> guesses;
    private Set<String> newDictionary;
    private Map<String, Set<String>> currentWords;
    private String currentOutput;


    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        newDictionary = new TreeSet<String>();
        guesses = new TreeSet<Character>();
        Scanner scan = new Scanner(dictionary);

        if (wordLength == 0) { throw new EmptyDictionaryException("Input a word"); }
        while (scan.hasNextLine()) {
            String current = scan.nextLine();
            if (current.length() == wordLength) { //add to the game
                newDictionary.add(current.toLowerCase());
            }
        }
        //usually because there aren't words of the specified length
        if (newDictionary.size() == 0) { throw new EmptyDictionaryException("No matching words"); }
    }

    public void play(int guesses) {
        int numGuesses = guesses;
        Scanner guess = new Scanner(System.in);
        int counter = 0;
        while (counter < numGuesses && currentOutput.contains("-")) {
            char next = guess.next().charAt(0);
            try {
                makeGuess(next);
            } catch (GuessAlreadyMadeException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess); //had to put here in order to pass tests
        if (guesses.contains(guess)) { //will put into GuessException
            throw new GuessAlreadyMadeException(guess + " has already been guessed!");
        }
        guesses.add(guess);

        currentWords = new TreeMap<>();
        for (String currentWord : newDictionary) {
            String testPattern = "";
            for (int i = 0; i < currentWord.length(); i++) {
                if (currentWord.charAt(i) == guess) testPattern += guess;
                else testPattern += '-';
            }
            if (currentWords.containsKey(testPattern)) { //already at least 1 word with this pattern
                currentWords.get(testPattern).add(currentWord);
            }
            else {
                SortedSet<String> newSet = new TreeSet<String>();
                newSet.add(currentWord);
                currentWords.put(testPattern, newSet);
            }
        }


        newDictionary = currentWords.get(getBestDictionary(guess));
        return newDictionary;
    }

    private String getBestDictionary(char guess) { //returns the best pattern from the currentWords map
        int max = 0;
        ArrayList<String> maxKeys = new ArrayList<String>(); //used arraylist just in case there are more than one of the max
        String key = "";
        boolean swap = false;
        for (String currentKey : currentWords.keySet()) {
            swap = false;
            if (currentWords.get(currentKey).size() > max) {
                max = currentWords.get(currentKey).size();
                key = currentKey;
                maxKeys = new ArrayList<String>(); //reset
                maxKeys.add(currentKey);
            }
            else if (currentWords.get(currentKey).size() == max) {
                int currentOccurrences = 0, occurrences = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == guess) occurrences++;
                    if (currentKey.charAt(i) == guess) currentOccurrences++;
                }
                if (currentOccurrences == 0) {
                    swap = true;
                }
            }
        }
//working on this currently
        /*if (swap) {
            max = currentWords.get(currentKey).size();
            key = currentKey;
            maxKeys = new ArrayList<String>(); //reset
            maxKeys.add(currentKey);
        } */
        if (maxKeys.size() == 1) return maxKeys.get(0);
        else { //more than one pattern with same amount of words
            int minOccurrences = maxKeys.get(0).length();
            for (String current : maxKeys) {
                int occurrences = 0;
                for (int i = 0; i < current.length(); i++) {
                    if (current.charAt(i) == guess) occurrences++;
                }
                if (occurrences == 0) {
                    return current;
                }
                else if (occurrences < minOccurrences) {
                    minOccurrences = occurrences;
                }
            }
        }

        return key;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guesses;
    }
}
