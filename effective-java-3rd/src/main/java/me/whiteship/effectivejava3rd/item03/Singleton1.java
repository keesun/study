package me.whiteship.effectivejava3rd.item03;

public class Singleton1 {

    public static final Singleton1 instance = new Singleton1();

    int count;

    private Singleton1() {
        count++;
        if (count != 1) {
            throw new IllegalStateException("this object should be singleton");
        }
    }

}
