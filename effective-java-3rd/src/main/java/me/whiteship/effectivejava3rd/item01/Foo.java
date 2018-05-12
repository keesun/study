package me.whiteship.effectivejava3rd.item01;

import java.util.EnumSet;

import static me.whiteship.effectivejava3rd.item01.Foo.Color.BLUE;
import static me.whiteship.effectivejava3rd.item01.Foo.Color.RED;
import static me.whiteship.effectivejava3rd.item01.Foo.Color.WHITE;

public class Foo {

    String name;

    String address;

    public Foo() {
    }

    private static final Foo GOOD_NIGHT = new Foo();

    public Foo(String name) {
        this.name = name;
    }

    public static Foo withName(String name) {
        return new Foo(name);
    }

    public static Foo withAddress(String address) {
        Foo foo = new Foo();
        foo.address = address;
        return foo;
    }

    public static Foo getFoo() {
        return GOOD_NIGHT;
    }

    public static Foo getFoo(boolean flag) {
        Foo foo = new Foo();

        // TODO 어떤 특정 약속 되어 있는 텍스트 파일에서 Foo의 구현체의 FQCN 을 읽어온다.
        // TODO FQCN 에 해당하는 인스턴스를 생성한다.
        // TODO foo 변수를 해당 인스턴스를 가리키도록 수정한다.

        return foo;
    }

    public static void main(String[] args) {
        Foo foo = new Foo("keesun");

        Foo foo1 = Foo.withName("keesun");

        Foo foo2 = Foo.getFoo();

        Foo foo3 = Foo.getFoo(false);

        EnumSet<Color> colors = EnumSet.allOf(Color.class);

        EnumSet<Color> blueAndWhite = EnumSet.of(BLUE, WHITE);
    }

    enum Color {
        RED, BLUE, WHITE
    }

    // private static method가 필요한 이유
    public static void doSomething() {
        // TODO 청소를 한다.
        // TODO 애들이랑 놀아준다.
        // TODO 저녁 약속에 간다.
        게임을하고잔다();
    }

    public static void doSomethingTomorrow() {
        // TODO 애들 데리고 수영장에 간다.
        // TODO 밥을 먹는다.
        게임을하고잔다();
    }

    private static void 게임을하고잔다() {
        // TODO 게임을 한다.
        // TODO 잔다.
    }

}
