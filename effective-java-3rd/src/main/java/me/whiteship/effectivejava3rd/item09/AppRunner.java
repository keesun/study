package me.whiteship.effectivejava3rd.item09;

public class AppRunner {

    public static void main(String[] args) {
        try(MyResource myResource = new MyResource();
            MyResource myResource1 = new MyResource()) {
            myResource.doSomething();
            myResource1.doSomething();
        }
    }
}
