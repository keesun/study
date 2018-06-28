package me.whiteship.effectivejava3rd.item03;

import java.io.Serializable;

public class Singleton2 implements Serializable {

    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {

    }

    public static Singleton2 getInstance() {
        return instance;
    }

}
