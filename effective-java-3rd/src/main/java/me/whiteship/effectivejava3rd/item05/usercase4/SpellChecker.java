package me.whiteship.effectivejava3rd.item05.usercase4;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpellChecker {

    private Lexicon lexicon;

    public SpellChecker(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public boolean isValid(String word) {
        lexicon.print();
        return true;
    }

    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

}
