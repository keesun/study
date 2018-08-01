package me.whiteship.effectivejava3rd.item08;

import java.lang.ref.Cleaner;

public class SampleResource implements AutoCloseable {

    private boolean closed;

    private static final Cleaner CLEANER = Cleaner.create();

    private final Cleaner.Cleanable cleanable;

    private final ResourceCleaner resourceCleaner;

    public SampleResource() {
        this.resourceCleaner = new ResourceCleaner();
        this.cleanable = CLEANER.register(this, resourceCleaner);
    }

    private static class ResourceCleaner implements Runnable {

        @Override
        public void run() {
            System.out.println("Clean");
        }
    }

    @Override
    public void close() throws RuntimeException {
        if (this.closed) {
            throw new IllegalStateException();
        }

        closed = true;
        cleanable.clean();
    }

    public void hello() {
        System.out.println("hello");
    }

}
