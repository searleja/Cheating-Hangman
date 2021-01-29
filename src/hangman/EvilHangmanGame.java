package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> guesses;
    private Set<String> newDictionary;
    private Map<String, Set<String>> currentWords;
    private String currentOutput = "";


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

    public void play(int nguesses, int wordLength) {
        int numGuesses = nguesses;
        Scanner guess = new Scanner(System.in);
        currentOutput = new String();
        for (int i = 0; i < wordLength; i++) {
            currentOutput += '-';
        }
        while (numGuesses > 0 && currentOutput.contains("-")) {
            System.out.println("You have " + numGuesses + " guesses left");
            System.out.print("Used letters: ");
            for (char c : guesses) {
                System.out.print(c + " ");
            }
            System.out.println("\nWord: " + currentOutput);
            System.out.print("Enter guess: ");
            String prevOutput = currentOutput;
            char next = guess.next().charAt(0);
            boolean valid = true;
            try {
                makeGuess(next);
            } catch (GuessAlreadyMadeException e) {
                valid = false;
                System.out.println(e);
            }

            updateOutput(next);

            if (prevOutput.equals(currentOutput)) {
                System.out.println("Sorry, there are no " + next + "'s");
                if (valid) numGuesses--;
            }
            else {
                int ctr = 0;
                for (int i = 0; i < currentOutput.length(); i++) {
                    if (currentOutput.charAt(i) == next) ctr++;
                }
                System.out.println("Yes, there is " + ctr + ' ' + next);
            }
            System.out.println();
        }
        if (!currentOutput.contains("-")) System.out.println("You Win!");
        else System.out.println("You Lose!");
        System.out.println("The word was: " + newDictionary.iterator().next());
    }

    private void updateOutput(char c) { //made a separate method as to not mess with unit tests
        String s = newDictionary.iterator().next();
        String temp = "";
        for (int i = 0; i < s.length(); i++) {
            if (currentOutput.charAt(i) != '-') temp += currentOutput.charAt(i);
            else if (s.charAt(i) == c) temp += s.charAt(i);
            else temp += '-';
        }
        currentOutput = temp;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess); //had to put here in order to pass tests
        if (guesses.contains(guess)) { //will put into GuessException
            throw new GuessAlreadyMadeException(guess + " has already been guessed!");
        }
        if (guess < 'a' || guess > 'z') {
            throw new GuessAlreadyMadeException(guess + " is not a valid input!");
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
        String key = "";
        boolean swap = false;
        for (String currentKey : currentWords.keySet()) {
            swap = false;
            if (currentWords.get(currentKey).size() > max) {
                swap = true;
            }
            else if (currentWords.get(currentKey).size() == max) {
                int currentOccurrences = 0, occurrences = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == guess) occurrences++;
                    if (currentKey.charAt(i) == guess) currentOccurrences++;
                }
                if (currentOccurrences == 0) swap = true; //no instances of the guess
                else if (currentOccurrences < occurrences) swap = true;
                else if (currentOccurrences == occurrences) {
                    int index = currentKey.length();
                    while (currentKey.lastIndexOf(guess, index) == key.lastIndexOf(guess, index)) {
                        if (currentKey.lastIndexOf(guess, index) > key.lastIndexOf(guess, index)) swap=true;
                        else if (currentKey.lastIndexOf(guess, index) == key.lastIndexOf(guess, index)) index = key.lastIndexOf(guess, index) - 1;
                    }
                }
            }
            if (swap) {
                max = currentWords.get(currentKey).size();
                key = currentKey;
            }
        }
        return key;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guesses;
    }
}
