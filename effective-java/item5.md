# 아이템 5: 리소스를 엮을 때는 의존성 주입을 선호하라

대부분의 클래스는 여러 리소스에 의존한다. 이 책에서는 `SpellChecker`와 `Dictionary`를 예로 들고 있다. 즉, `SpellCheck`가 `Ditionary`를 사용하고, 이를 의존하는 리소스 또는 의존성이라고 부른다. 이때 `SpellChecker`를 다음과 같이 구현하는 경우가 있다.

## 부적절한 구현

### static 유틸 클래스 ([아이템4](item4.md))

```java
// 부적절한 static 유틸리티 사용 예 - 유연하지 않고 테스트 할 수 없다.
public class SpellChecker {

    private static final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
        // Noninstantiable
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {}

class KoreanDicationry implements Lexicon {}
```

### 싱글톤으로 구현하기 ([아이템3](item3.md))

```java
// 부적절한 싱글톤 사용 예 - 유연하지 않고 테스트 할 수 없다.
public class SpellChecker {

    private final Lexicon dictionary = new KoreanDicationry();

    private SpellChecker() {
    }

    public static final SpellChecker INSTANCE = new SpellChecker() {
    };

    public boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }


    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

}
```

사전을 하나만 사용할꺼라면 위와 같은 구현도 만족스러울 수 있겠지만, 실제로는 각 언어의 맞춤법 검사기는 사용하는 사전이 각기 다르다. 또한 테스트 코드에서는 테스트용 사전을 사용하고 싶을 수도 있다.

**어떤 클래스가 사용하는 리소스에 따라 행동을 달리 해야 하는 경우에는 스태틱 유틸리티 클래스와 싱글톤을 사용하는 것은 부적절하다.**

그런 요구 사항을 만족할 수 있는 간단한 패턴으로 생성자를 사용해서 새 인스턴스를 생성할 때 사용할 리소스를 넘겨주는 방법이 있다.

## 적절한 구현

```java
public class SpellChecker {

    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }
    
    public List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

}

class Lexicon {}
```

위와 같은 의존성 주입은 생성자, 스태틱 팩토리([아이템1](item1.md)) 그리고 빌더([아이템2](item2.md))에도 적용할 수 있다.

이 패턴의 변종으로 리소스의 팩토리를 생성자에 전달하는 방법도 있다. 이 방법은 자바 8에 들어온 `Supplier<T>` 인터페이스가 그런 팩토리로 쓰기에 완벽하다. `Supplier<T>`를 인자로 받는 메서드는 보통 `bounded wildcard type` ([아이템31](item31.md))으로 입력을 제한해야 한다.

```java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```

의존성 주입이 유연함과 테스트 용이함을 크게 향상 시켜주지만, 의존성이 많은 큰 프로젝트인 경웅에는 코드가 장황해 질 수 있다. 그점은 대거, 쥬스, 스프링 같은 프레임웍을 사용해서 해결할 수 있다.

요약하자면 의존하는 리소스에 따라 행동을 달리하는 클래스를 만들 때는 싱글톤이나 스태틱 유틸 클래스를 사용하지 말자. 그런 경우에는 리소스를 생성자나 팩토리로 전달하는 의존성 주입을 사용하여 유연함, 재사용성, 테스트 용이성을 향상 시키자.

## 참고

* [팩토리 메소드 패턴](https://en.wikipedia.org/wiki/Factory_method_pattern)
* [Supplier 인터페이스](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)
