# Item 44. 멤버 확장 함수의 사용을 피하라

- - -

* 어떤 클래스에 대한 확장 함수를 정의할 때, 이를 멤버로 추가하는 것은 좋지 않다.
* 확장 함수는 첫 번째 아규먼트로 리시버를 받는 일반 함수로 컴파일된다.

```kotlin
fun String.isPhoneNumber(): Boolean =
    length == 7 && all { it.isDigit() }

// 위 함수가 아래와 같이 컴파일 된다.

fun isPhoneNumber('$this': String): Boolean =
    '$this'.length == 7 && '$this'.all { it.isDigit() }
```

* 위와 같이 단순하게 변환되는 것이므로, 확장 함수를 클래스 멤버나 인터페이스 내부에 정의할 수도 있다.
    * 하지만 DSL을 만들 때를 제외하고 이를 사용하지 않는 것이 좋다.
    * 특히 가시성 제한을 위해 확장 함수를 멤버로 정의하는 것은 좋지 않다.
    * 한 가지 큰 이유는 가시성을 제한하지 못한다는 것이다.
    * 확장 함수의 가시성을 제한하고 싶다면, 멤버로 만들지 말고, 가시성 한정자를 붙여 주면 된다.

### 멤버 확장을 피해야 하는 이유

* 레퍼런스를 지원하지 않는다.

```kotlin
val ref = String::isPhoneNumber
val str = "1234567890"
val boundedRef = str::isPhoneNumber

val refX = PhoneBookIncorrect::isPhoneNumber    // 오류
val book = PhoneBookIncporrect()
val boundedRefX = book::isPhoneNumber   // 오류
```

* 암묵적 접근을 할 때, 두 리시버 주엥 어떤 리시버가 선택될지 혼동된다.

```kotlin
class A {
    val a = 10
}
class B {
    val a = 20
    val b = 30
  
  fun A.test() = a + b  // 40? 50?
}
```
* 확장 함수가 외부에 있는 다른 클래스를 리시버로 받을 때, 해당 함수가 어떤 동작을 하는지 명확하지 않다.
```kotlin
class A {
    // ...
}
class B {
    // ...
    
    fun A.update() = { /* ...  */ } // A와 B 중에서 어떤 것을 업데이트?
}
```
* 경험이 적은 개발자의 경우 확장 함수를 보면, 직관적이지 않거나, 심지어 보기만 해도 겁먹을 수도 있다.
