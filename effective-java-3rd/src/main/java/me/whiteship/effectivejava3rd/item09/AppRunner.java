package me.whiteship.effectivejava3rd.item09;

public class AppRunner {

    public static void main(String[] args) {
        MyResource myResource = new MyResource();
        try {
            myResource.doSomething();
        } catch (FirstError error) {
            System.out.println("first error");
            throw error;
        } finally {
            myResource.close();
        }
    }
}
