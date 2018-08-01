# 아이템 8: Finalizer와 Cleaner는 피하라

**Finalizer는 예측 불가능하고, 위험하며, 대부분 불필요하다.** 그걸 쓰면 이상하게 동작하기도 하고, 성능도 안좋아지고, 이식성에도 문제가 생길 수 있다. Finalizer를 유용하게 쓸 수 있는 경우는 극히 드물다.

딱 두가지 경우
* 안전망 역할로 자원을 반납하고자 하는 경우.
* 네이티브 리소스를 정리해야 하는 경우.

일단 자바 9에서는 Finalizer가 deprecated 됐으면 `Cleaner`라는게 새로 생겨서 Finalizer 보다 덜 위험하지만(별도의 쓰레드를 사용하니까), 여전히 예측 불가능하며, 느리고, 일반적으로 불필요하다.

C++에서의 destructor랑은 다른거다. C++의 destructor는 어떤 객체와 연관이는 자원을 반납할 때도 사용하지만, 자바에서는 `try-with-resources`또는 `try-finally` 블럭이 그 역할을 한다.

## 단점 1

언제 실행될지 알 수 없다. 어떤 객체가 더이상 필요 없어진 시점에 그 즉시 finalizer 또는 cleaner가 바로 실행되지 않을 수도 있다. 그 사이에 시간이 얼마나 걸릴지는 아무도 모른다. 따라서 **타이밍이 중요한 작업을 절대로 finalizer나 cleaner에서 하면 안된다.** 예를 들어, 파일 리소스를 반납하는 작업을 그 안에서 처리한다면, 실제로 그 파일 리소스 처리가 언제 될지 알 수 없고, 자원 반납이 안되서 더이상 새로운 파일을 열 수 없는 상황이 발생할 수도 있다.

## 단점 2

특히 Finalizer는 인스턴스 반납을 지연 시킬 수도 있다. Finalizer 쓰레드는 우선 순위가 낮아서 언제 실행될지 모른다. 따라서, Finalizer 안에 어떤 작업이 있고, 그 작업을 쓰레드가 처리 못해서 대기하고 있다면, 해당 인스턴스는 GC가 되지 않고 계속 쌓이다가 결국엔 OutOfMomoryException이 발생할 수도 있다.

Clenaer는 별도의 쓰레드로 동작하니까 이 부분에 있어서 조금은 나을 수도 있지만 (해당 쓰레드의 우선순위를 높게 준더거나..), 여전히 해당 쓰레드는 백그라운드에서 동작하고 언제 처리될지는 알 수 없다.

## 단점 3

즉시 실행되지 않는 다는건 차치하고 Finalizer나 Cleaner를 아에 실행하지 않을 수도 있다. 따라서, **Finalizer나 Cleaner로 저장소 상태를 변경하는 일을 하지 말라.** 데이터베이스 같은 자원의 락을 그것들로 반환하는 작업을 한다면 전체 분산 시스템이 멈춰 버릴 수도 있다.

`System.gc`나 `System.runFinalization`에 속지 말라. 그걸 실행해도 Finalizer나 Cleaner를 바로 실행한다고 보장하진 못한다. 그걸 보장해주겠다고 만든 `System.runFinalizersOnExit`와 그 쌍둥이 `Runtime.runFinalizersOnExit`은 둘다 망했고 수십년간 deprecated 상태다.

## 단점 4

심각한 성능 문제도 있다. `AutoCloseable` 객체를 만들고, `try-with-resource`로 자원 반납을 하는데 걸리는 시간은 12ns 인데 반해, Finalizer를 사용한 경우에 550ns가 걸렸다. 약 50배가 걸렸다. Cleaner를 사용한 경우에는 66ns가 걸렸다 약 5배.

## 단점 5

Finalizer 공격이라는 심각한 보안 이슈에도 이용될 수 있다. 어떤 클래스가 있고 그 클래스를 공격하려는 클래스가 해당 클래스를 상속 받는다. 그리고 그 나쁜 클래스의 인스턴스를 생성하는 도중에 예외가 발생하거나, 직렬화 할 때 예외가 발생하면, 원래는 죽었어야 할 객체의 finalizer가 실행될 수 있다. 그럼 그 안에서 해당 인스턴스의 레퍼런스를 기록할 수도 있고, GC가 되지 못하게 할 수도 있다. 또한 그 안에서 인스턴스가 가진 메소드를 호출할 수도 있다.

원래는 생성자에서 예외가 발생해서 존재하질 않았어야 하는 인스턴스인데, Finalizer 때문에 살아 남아 있는 것이다.

Final 클래스는 상속이 안되니까 근본적으로 이런 공격이 막혀 있으며, 다른 클래스는 `finalize()` 메소드에 `final` 키워드를 사용해서 상속해서 오버라이딩 하는 것을 만을 수 있다.

## 자원 반납 하는 방법

자원 반납이 필요한 클래스 `AutoCloseable` 인터페이스를 구현하고 `try-with-resource`를 사용하거나, 클라이언트가 `close` 메소드를 명시적으로 호출하는게 정석이다. 여기서 추가로 언급하자면, `close` 메소드는 현재 인스턴스의 상태가 이미 종료된 상태인지 확인하고, 이미 반납이 끝난 상태에서 `close`가 다시 호출됐다면 `IllegalStateException`을 던져야 한다.

## Finalizer와 Cleaner를 안전망으로 쓰기

자원 반납에 쓸 `close` 메소드를 클라이언트가 호출하지 않았다는 가정하에, 물론 실제로 Finalizer나 Cleaner가 호출될지 안될지 언제 호출될지도 모르긴 하지만, 안하는 것 보다는 나으니까. 실제로 자바에서 제공하는 `FileInputStream`, `FileOutputStream`, `ThreadPoolExecutor` 그리고 `java.sql.Connection`에는 안전망으로 동작하는 finalizer가 있다.

## 네이티브 피어 정리할 때 쓰기

```
자바 클래스 -> 네이티브 메소드 호출 -> 네이티브 객체 (네티이브 Peer)
```

네이티브 객체는 일반적인 객체가 아니라서 GC가 그 존재를 모른다. 네이티브 피어가 들고 있는 리소스가 중요하지 않은 자원이며, 성능상 영향이 크지 않다면 Cleaner나 Finalizer를 사용해서 해당 자원을 반납할 수도 있을 것이다. 하지만, 중요한 리소스인 경우에는 위에서 언급한대로 `close` 메소드를 사용하는게 좋다.

## Cleaner 예제 코드

```
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
```
주의 할 점
* Cleaner 쓰레드(`CleanerRunner`)는 정리할 대상인 인스턴스 (`CleanerSample`)을 참조하면 안된다. 순환 참조가 생겨서 GC 대상이 되질 못한다.
* Cleaner 쓰레드를 만들 클래스는 반드시 static 클래스여야 한다. non-static 클래스(익명 클래스도 마찬가지)의 인스턴스는 그걸 감싸고 있는 클래스의 인스턴스를 잠조하지 않는다.

## 참고
* [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)
* [Try-With-Resource]( https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)
* [Cleaner](https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html)
* [Cleaner 예제](https://www.logicbig.com/tutorials/core-java-tutorial/gc/ref-cleaner.html)
