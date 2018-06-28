package me.whiteship.effectivejava3rd.item05.usercase4;

import org.springframework.stereotype.Component;

@Component
public class KoreanDictionary implements Lexicon {

    @Override
    public void print() {
        System.out.println("Korean");
    }

}
