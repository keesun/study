package me.whiteship.effectivejava3rd.item04.usecase2;

import java.util.List;

public class SpellChecker {

    private final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
    }

    public static final SpellChecker INSTANCE = new SpellChecker() {
    };

    public boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

}


interface Lexicon {}

class KoreanDicationry implements Lexicon {}
