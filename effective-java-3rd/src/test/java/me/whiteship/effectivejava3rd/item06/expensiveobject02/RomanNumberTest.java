package me.whiteship.effectivejava3rd.item06.expensiveobject02;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RomanNumberTest {

    @Test
    public void isRomanNumeral() {
        assertThat(RomanNumber.isRomanNumeral("IV")).isTrue();
        assertThat(RomanNumber.isRomanNumeral("XX")).isTrue();
        assertThat(RomanNumber.isRomanNumeral("IIIIV")).isFalse();
    }


}