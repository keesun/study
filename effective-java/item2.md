# 아이템 2: 생성자 매개변수가 많은 경우에 빌더 사용을 고려해 볼 것

[![생성자 매개변수가 많은 경우에 빌더 사용을 고려해 볼 것](https://img.youtube.com/vi/OwkXMxCqWHM/0.jpg)](https://youtu.be/OwkXMxCqWHM)

static 팩토리 메소드와 public 생성자 모두 매개변수가 많이 필요한 경우에 불편해진다. `NutritiaonFact`라는 클래스를 예로 들고있다. 해당 클래스는 몇몇 반드시 필요한 필드와 부가적인 필드를 가질 수 있는데, 그런 경우에 필수적인 매개변수를 가진 생성자에 부가적인 필드를 하나씩 추가하며 여러 생성자를 만들 수 있다.

## 해결책 1: 생성자

그리고 설정하고 싶은 매개변수를 최소한으로 사용하는 생성자를 사용해서 인스턴스를 만들 수 있다.

```java
NutritionFacts cocaCola =
new NutritionFacts(240, 8, 100, 0, 35, 27);
```

이런 생성자를 쓰다보면 필요없는 매개변수도 넘겨야 하는 경우가 발생하는데, 보통 0 같은 기본값을 넘긴다. 물론 이런 방법이 동작하긴 하지만 **이런 코드는 작성하기도 어렵고 읽기도 어렵다**.

## 해결책 2: 자바빈

또 다른 대안으로는 아무런 매개변수를 받지 않는 생성자를 사용해서 인스턴스를 만들고, 세터를 사용해서 필요한 필드만 설정할 수 있다.

```java
NutritionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setSodium(35);
cocaCola.setCarbohydrate(27);
```

이 방법의 단점은 최종적인 인스턴스를 만들기까지 여러번의 호출을 거쳐야 하기 때문에 **자바빈이 중간에 사용되는 경우 안정적이지 않은 상태로 사용될 여지가 있다**. 또한 (게터와 세터가 있어서) 불변 클래스([아이템 17](item17.md))로 만들지 못한다는 단점이 있고 (쓰레드 간에 공유 가능한 상태가 있으니까) 쓰레드 안정성을 보장하려면 추가적인 수고 (locking 같은) 가 필요하다.

그런 담점을 보완할 수 있는 방법으로 객체를 "Freezing" 할 수 있다는데.. 이 부분은 어떻게 한다는건지 이해가 안갑니다.

## 해결책 3: 빌더

신축적인 (Telescoping, 여기선 필수적인 매개변수와 부가적인 매개변수 조합으로 여러 생성자를 만들 수 있다는 것을 의미하는 단어로 쓰인듯 합니다.) 생성자의 안정성과 자바빈을 사용할때 얻을 수 있었던 가독성을 모두 취할 수 있는 대안이 있다. 바로 빌더 패턴이다.

빌더 패턴은 만들려는 객체를 바로 만들지 않고 클라이언트는 빌더(생성자 또는 static 팩토리)에 필수적인 매개변수를 주면서 호출해 `Builder` 객체를 얻은 다음 빌더 객체가 제공하는 세터와 비슷한 메소드를 사용해서 부가적인 필드를 채워넣고 최종적으로 `build`라는 메소드를 호출해서 만들려는 객체를 생성한다.

```java
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
.calories(100).sodium(35).carbohydrate(27).build();
```
빌더 패턴으로 파이썬이나 스칼라가 제공하는 `Named Optional Parameter`를 모방할 수 있다.

빌더의 생성자나 메소드에서 유효성 확인을 할 수도 있고 여러 매개변수를 혼합해서 확인해야 하는 경우에는 `build` 메소드에서 호출하는 생성자에서 할 수 있다. 빌더에서 매개변수를 객체로 복사해온 다음에 확인하고 ([아이템 50](item50.md)), 검증에 실패하면 `IllegalArgumentException`을 던지고 ([아이템 72](item72.md)) 에러 메시지로 어떤 매개변수가 잘못됐는지 알려줄 수 있다. ([아이템 75](item75.md))

클래스 계층 구조를 잘 활용할 수 있다. 추상 빌더를 가지고 있는 추상 클래스를 만들고 하위 클래스에서는 추상 클래스를 상속받으며 각 하위 클래스용 빌더도 추상 빌더를 상속받아 만들 수 있다.

```java
public abstract class Pizza {

    public enum Topping {
        HAM, MUSHROOM, ONION, PEEPER, SAUSAGE
    }

    final Set<Topping> toppings;

    abstract static class Builder<T extends  Builder<T>> { // `재귀적인 타입 매개변수`
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build(); // `Convariant 리턴 타입`을 위한 준비작업

        protected abstract T self(); // `self-type` 개념을 사용해서 메소드 체이닝이 가능케 함
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }

}
```

```java
public class NyPizza extends Pizza {

    public enum Size {
        SMALL, MEDIUM, LARGE
    }

    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }


        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}
```

```java
public class Calzone extends Pizza {

    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauseInside = false;

        public Builder sauceInde() {
            sauseInside = true;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauseInside;
    }

}
```

이때 추상 빌더는 `재귀적인 타입 매개변수`를 사용하고 `self`라는 메소드를 사용해 `self-type` 개념을 모방할 수 있다. 하위 클래스에서는 `build` 메소드의 리턴 타입으로 해당 하위 클래스 타입을 리턴하는 `Covariant 리턴 타이핑`을 사용하면 클라이언트 코드에서 타입 캐스팅을 할 필요가 없어진다.

```java
NyPizza nyPizza = new NyPizza.Builder(SMALL)
    .addTopping(Pizza.Topping.SAUSAGE)
    .addTopping(Pizza.Topping.ONION)
    .build();

Calzone calzone = new Calzone.Builder()
    .addTopping(Pizza.Topping.HAM)
    .sauceInde()
    .build();
```

빌더는 가변 인자 (vargars) 매개변수를 여러개 사용할 수 있다는 소소한 장점도 있다. (생성자나 팩토리는 가변인자를 맨 마지막 매개변수에 한번밖에 못쓰니까요.)  또한 토핑 예제에서 본것처럼 여러 메소드 호출을 통해 전달받은 매개변수를 모아 하나의 필드에 담는 것도 가능하다.

빌더는 꽤 유연해서 빌더 하나로 여러 객체를 생성할 수도 있고 매번 생성하는 객체를 조금씩 변화를 줄 수도 있다. 만드는 객체에 시리얼 번호를 증가하는 식으로.

단점으로는 객체를 만들기 전에 먼저 빌더를 만들어야 하는데 성능에 민감한 상황에서는 그점이 문제가 될 수도 있다. 그리고 생성자를 사용하는 것보다 코드가 더 장황하다. 따라서 빌더 패턴은 매개변수가 많거나(4개 이상?) 또는 앞으로 늘어날 가능성이 있는 경우에 사용하는것이 좋다.

## 참고

* [롬복 @Builder](https://projectlombok.org/features/Builder)
* [JS Object.freeze()](https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Global_Objects/Object/freeze)