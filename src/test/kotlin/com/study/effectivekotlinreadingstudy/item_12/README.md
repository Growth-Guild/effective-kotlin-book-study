# Item 12. 연산자 오버로드를 할 때는 의미에 맞게 사용하라

- - -

* 특정한 목적을 위해 일반적으로 통용되는 연산자의 의미를 바꾸어서는 안된다. 이는 코드를 읽을 때, 컨텍스트 마다 그 의미를 해석하게 만들기 떄문에 이해하기 어렵다.
* 연산자는 구체적인 이름을 가진 함수이며, 사용자는 이러한 연산자의 이름이 나타내는 역할을 할 거라고 기대하기 때문에 관례를 지키는 것은 중요하다.

- - -

### 분명하지 않은 경우
* 관례를 충족하는지 아닌지 확실하지 않은 경우, infix를 활용한 확장 함수를 사용하는 것이 좋다.
```kotlin
operator fun Int.times(operation: () -> Unit) {
    repeat(this) { operation() }
}

3 * { print("Hello") }  // HelloHelloHello

// 위 코드는 아래와 같이 infix를 활용하여 의미를 좀더 분명하게 전달할 수 있다.
infix fun Int.timesRepeated(operation: () -> Unit) = {
    repeat(this) { operation() }
}

val tripledHello = 3 timesRepeated { print("Hello") }
tripledHello()  // HelloHelloHello
```
* 톱레벨 함수(top-level function)을 사용하는 것도 좋다. (함수를 n번 호출하는 것은 다음과 같은 형태로 이미 stdlib에 구현되어 있다.)
```kotlin
repeat(3) { println("Hello") }  // HelloHelloHello
```

- - -

### 규칙을 무시해도 되는 경우
* 연산자 오버로딩 규칙을 무시해도 되는 중요한 경우는 도메인 특화 언어(DSL: Domain Specific Language)를 설계할 때이다.
* 아래의 경우는 HTML DSL 예시이다.
```kotlin
body {
    div {
        +"Some text"
    }
}
```
