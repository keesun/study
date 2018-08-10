# Try-Finally 대신 Try-with-Resource 사용하라

자바 라이브러리에는 `InputStream`, `OutputStream` 그리고 `java.sql.Connection`과 같이 정리(close)가 가능한 리소스가 많은데, 그런 리소스를 사용하는 클라이언트 코드가 보통 리소스 정리를 잘 안하거나 잘못하는 경우가 있다.

```java
public class FirstError extends RuntimeException {
}
```

```java
public class SecondException extends RuntimeException {
}
```

```java
public class MyResource implements AutoCloseable {

    public void doSomething() throws FirstError {
        System.out.println("doing something");
        throw new FirstError();
    }

    @Override
    public void close() throws SecondException {
        System.out.println("clean my resource");
        throw new SecondException();
    }
}
```

```java
MyResource myResource = null;
try {
    myResource = new MyResource();
    myResource.doSomething();
} finally {
    if (myResource != null) {
        myResource.close();
    }
}
```

이 코드에서 예외가 발생하면 `SecondException`이 출력되고 `FirstException`은 덮힌다. 즉 안 보인다. 그러면 문제를 디버깅하기 힘들어 진다. 또한 중복으로 try-catch를 만들어여 하는 경우에도 실수를 할 가능성이 높다. (자바 퍼즐러 88쪽 참고)

자바7에 추가된 Try-with-Resource를 사용하면 코드 가독성도 좋고, 문제를 분석할 때도 훨씬 좋다. 왜냐면 Try-Finally를 사용할 때 처럼 처음에 발생한 예외가 뒤에 발생한 에러에 덮히지 않으니까.

뒤에 발생한 에러는 첫번째 발생한 에러 뒤에다 쌓아두고(suppressed) 처음 발생한 에러를 중요시 여긴다. 그리고 `Throwable`의 `getSuppressed` 메소드를 사용해서 뒤에 쌓여있는 에러를 코딩으로 사용할 수도 있다.

`catch` 블록은 Try-Fianlly와 동일하게 사용할 수 있다.

# 참고
* [자바 퍼즐러 88쪽](https://stackoverflow.com/questions/48449093/what-is-wrong-with-this-java-puzzlers-piece-of-code)

