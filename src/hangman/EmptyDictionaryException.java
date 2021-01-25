package hangman;

import java.io.File;

public class EmptyDictionaryException extends Exception {
	//Thrown when dictionary file is empty or no words in dictionary match the length asked for
    public EmptyDictionaryException(File dictionary) {
        if (dictionary.length() == 0) { }
    }

}
