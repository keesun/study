package me.whiteship.effectivejava3rd.item08;

public class CleanerSampleClient {

    public static void main(String[] args) {
        try(var cleanerSample = new CleanerSample()) {
            cleanerSample.doSomething();
        }
    }
}
