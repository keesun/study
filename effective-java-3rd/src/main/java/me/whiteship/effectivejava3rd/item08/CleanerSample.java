package me.whiteship.effectivejava3rd.item08;


import java.lang.ref.Cleaner;

public class CleanerSample implements AutoCloseable {

    private static final Cleaner CLEANER = Cleaner.create();

    private final CleanerRunner cleanerRunner;

    private final Cleaner.Cleanable cleanable;

    public CleanerSample() {
        cleanerRunner = new CleanerRunner();
        cleanable = CLEANER.register(this, cleanerRunner);
    }

    @Override
    public void close() {
        cleanable.clean();
    }

    public void doSomething() {

        System.out.println("do it");
    }

    private static class CleanerRunner implements Runnable {

        // TODO 여기에 정리할 리소스 전달

        @Override
        public void run() {
            // 여기서 정리
            System.out.printf("close");
        }
    }

}
