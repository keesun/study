package me.whiteship.effectivejava3rd.item04.usecase1;

import java.util.List;

public class SpellChecker {

    private static final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
        // Noninstantiable
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}


interface Lexicon {}

class KoreanDicationry implements Lexicon {}