package me.whiteship.effectivejava3rd.item08;

public class FinalizerExample {

    @Override
    protected final void finalize() throws Throwable {
        System.out.println("Clean up");
    }

    public void hello() {
        System.out.println("hi");
    }
}
