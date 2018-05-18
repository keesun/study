# 아이템 7: 더이상 쓰지 않는 객체 레퍼런스는 없애자

자바에 GC (가비지 콜렉터)가 있기 때문에 메모리 관리에 대해 신경쓰지 않아도 될거라고 생각하기 쉽지만 그렇지 않다. 다음 코드를 살펴보자.

```java
// Can you spot the "memory leak"?
public class Stack {

    private Object[] elements;

    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        this.ensureCapacity();
        this.elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        return this.elements[--size]; // 주목!!
    }

    /**
     * Ensure space for at least one more element,
     * roughly doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (this.elements.length == size) {
            this.elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

스택에 계속 쌓다가 많이 빼냈다고 치자, 그래도 스택이 차지하고 있는 메모리는 줄어들지 않는다. 왜냐면 저 스택의 구현체는 필요없는 객체에 대한 레퍼런스를 그대로 가지고 있기 때문이다. 가용한 범위는 `size` 보다 작은 부분이고 그 값 보다 큰 부분에 있는 값들은 필요없이 메모리를 차지하고 있는 부분이다.

다음과 같이 코드를 수정할 수 있다.

```java
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        Object value = this.elements[--size];
        this.elements[size] = null;
        return value;
    }
```

스택에서 꺼낼 때 그 위치에 있는 객체를 꺼내주고 그 자리를 `null`로 설정해서 다음 GC가 발생할 때 레퍼런스가 정리되게 한다. 실수로 해당 위치에 있는 객체를 다시 꺼내는 경우에 `NullPointerException`이 발생할 수 있긴 하지만, 그 자리에 있는 객체를 비우지 않고 실수로 잘못된 객체를 돌려주는 것 보다는 차라리 괜찮다. 프로그래밍 에러는 언제든지 가능한한 빨리 포착하는 것이 유익하다. (Fail-Fast?)

그렇다고 필요없는 객체를 볼 떄 마다 `null`로 설정하는 코드를 작성하지는 말자. **객체를 Null로 설정하는 건 예외적인 상황에서나 하는것이지 평범한 일이 아니다.** 필요없는 객체 레퍼런스를 정리하는 최선책은 그 레퍼런스를 가리키는 변수를 특정한 범위(스코프) 안에서만 사용하는 것이다. (로컬 변수는 그 영역 넘어가면 쓸모 없어져서 정리되니까요.) 변수를 가능한 가장 최소의 스콥으로 사용하면 자연스럽게 그렇게 될 것이다. (하지만 위에 코드처럼 size라는 멤버 변수와 elements를 쓰는 경우엔 역시 자연스럽게 그렇게 되진 않으니까.. 즉 예외적인 상황이라 그래서 명시적으로 null로 설정하는 코드를 써줘야 했던 겁니다.)

그럼 언제 레퍼런스를 `null`로 설정해야 하는가? **메모리를 직접 관리 할 때.** `Stack` 구현체ㅓ럼 `elements`라는 배열을 관리하는 경우에 GC는 어떤 객체가 필요없는 객체인지 알 수 없다. 오직 프로그래머만 `elements`에서 가용한 부분 (size 보다 작은 부분)과 필요없는 부분 (size 보다 큰 부분)을 알 수 있다. 따라서, 프로그래머가 해당 레퍼런스를 null로 만들어서 GC한테 필요없는 객체들이라고 알려줘야 한다.

**메모리를 직접 관리하는 클래스는 프로그래머가 메모리 누수를 조심해야 한다.**

