package me.whiteship.effectivejava3rd.item06.strings;

public class StringTest {

    public static void main(String[] args) {
        Boolean true1 = Boolean.valueOf("true");
        Boolean true2 = Boolean.valueOf("true");

        System.out.println(true1 == true2);
        System.out.println(true1 == Boolean.TRUE);
    }
}
