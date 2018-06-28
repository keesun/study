package me.whiteship.effectivejava3rd.item06.autoboxing;

public class AutoBoxingExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long sum = 0l;
        for (long i = 0 ; i <= Integer.MAX_VALUE ; i++) {
            sum += i;
        }
        System.out.println(sum);
        System.out.println(System.currentTimeMillis() - start);
    }
}
