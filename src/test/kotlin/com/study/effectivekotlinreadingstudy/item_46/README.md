# Item 46. 함수 타입 파라미터를 갖는 함수에 inline 한정자를 붙여라

- - -

* 코틀린 표준 라이브러리의 고차 함수를 살펴보면, 대부분 inline 한정자가 붙어 있는 것을 볼 수 있다.

```kotlin
inline fun repeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}
```

* inline 한정자의 역할은 컴파일 시점에 '함수를 호출하는 부분'을 '함수의 본문'으로 대체하는 것이다.

```kotlin
repeat(10) {
    println(it)
}
// 예를 들어 위와 같이 repeat 함수를 호출하는 코드가 있다면, 컴파일 시점에 아래와 같이 대체된다.

for (index in 0 until 10) {
    println(index)
}
```

* inline 함수를 사용하면 다음과 같은 장점이 있다.
    * 타입 아규먼트에 reified 한정자를 붙여서 사용할 수 있다.
    * 함수 타입 파라미터를 가진 함수가 훨씬 빠르게 동작한다.
    * 비지역(non-local) 리턴을 사용할 수 있다.

### 타입 아규먼트를 reified로 사용할 수 있다.

* JVM 바이트 코드에는 제네릭이 존재하지 않으므로, 컴파일을 하면 제네릭 타입과 관련된 내용이 제거된다.
* 함수를 인라인으로 만들면, 이러한 제한을 무시할 수 있다.
* 함수 호출이 본문으로 대체되므로, reified 한정자를 지정하면, 타입 파라미터를 사용한 부분이 타입 아규먼트로 대체된다.

```kotlin
inline fun <reified T> printTypeName() {
    println(T::class.simpleName)
}

// 사용
printTypeName<Int>() // Int
printTypeName<Char>() // Char
printTypeName<String>() // String
```

* 컴파일하는 동안 printTypeName의 본문이 아래와 같이 대체된다.

```kotlin
println(Int::class.simpleName)
println(Char::class.simpleName)
println(String::class.simpleName)
```

### 함수 타입 파라미터를 가진 함수가 훨씬 빠르게 동작한다.

* 모든 함수는 inline 한정자를 붙이면 조금 더 빠르게 동작한다.
    * 함수 호출과 리턴을 위해 점프하는 과정과 백스택을 추적하는 과정이 없기 때문이다.
* 하지만 함수 파라미터를 가지지 않는 함수에서는 이러한 차이가 큰 성능 차이를 발생시키지는 않는다.
* 코틀린/JVM에서 JVM에서는 JVM 익명 클래스 또는 일반 클래스를 기반으로, 함수를 객체로 만들어 낸다.

```kotlin
// kotlin
val lambda: () -> Unit = {
    // ...
}

```

* 위 코드는 아래와 같이 컴파일 된다.

```java
// java
Function0<Unit> lambda=new Function0<Unit>(){
public Unit invoke(){
        // ...
        }
        }
```

* 별도의 파일에 정의되어 있는 일반 클래스로 컴파일하면 다음과 같다.

```java
// java
public class Test$lambda implements Function0<Unit> {
    public Unit invoke() {
        // ...
    }
}

    // 사용
    Function0 lambda = new Test$lambda()
```

* JVM에서 함수 타입은 다음과 같은 형태로 변환된다.
* () -> Unit는 Function0<Unit>로 컴파일
* () -> Int는 Function0<Int>로 컴파일
* (Int) -> Int는 Function1<Int, Int>로 컴파일
* (Int, Int) -> Int는 Function2<Int, Int, Int>로 컴파일
  <br>
* 이러한 모든 인터페이스는 모두 코틀린 컴파일러에 의해서 생성된다.
* 요청이 있을 때 생성되므로, 이를 명시적으로 사용할 수는 없다.
* 함수 본문을 객체로 랩(wrap)하면, 코드의 속도가 느려진다. 따라서 다음 두 함수에서 첫 번째 함수가 더 빠르다.

```kotlin
inline fun repeat(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}

fun repeatNoinline(times: Int, action: (Int) -> Unit) {
    for (index in 0 until times) {
        action(index)
    }
}
```

* '인라인 함수'와 '인라인 함수가 아닌 함수'의 더 중요한 차이는 함수 리터럴 내부에서 지역 변수를 캡처할 때 확인할 수 있다.
* 캡처된 값은 객체로 래핑(wrapping)해야 하며, 사용할 때마다 겍체를 통해 작업이 이루어져야 한다.
* 인라인이 아닌 람다 표현식에서는 지역 변수를 직접 사용할 수 없다. 지역 변수는 컴파일 과정 중에 레퍼런스 객체로 래핑되고, 람다 표현식 내부에서는 이를 사용한다.

### 비지역적 리턴(non-local return)을 사용할 수 있다

* 함수 리터럴이 컴파일 될 때, 함수가 객체로 래핑되기 때문에 inline이 아닌 함수는 return을 사용할 수 없다.
* 함수가 다른 클래스에 위치하므로, return을 사용해서 main으로 돌아올 수 없기 때문이다.
* 인라인 함수는 이러한 제한이 없다. 함수가 main 함수 내부에 박히기 때문이다.

### inline 한정자의 비용

* 인라인 함수는 재귀적으로 동작할 수 없다. 재귀적으로 사용하면 무한히 대체되는 문제가 발생하기 때문이다.
* 인라인 함수는 더 많은 가시성 제한을 가진 요소를 사용할 수 없다.
    * public 인라인 함수 내부에서는 private와 internal 가시성을 가진 함수와 프로퍼티를 사용할 수 없다.
* inline 한정자를 남용하면, 코드의 크기가 쉽게 커지므로 주의해야 한다.

### crossinline과 noinline

* 함수를 인라인으로 만들고 싶지만, 어떤 이유로 일부 함수 타입 파라미터는 inline으로 받고 싶지 않은 경우가 있을 수 있다.
* 이러한 경우 다음과 같은 한정자를 이용한다.
* crossinline
    * 아규먼트로 인라인 함수를 받지만, 비지역적 리턴을 하는 함수는 받을 수 없게 된다.
    * 인라인으로 만들지 않은 다른 람다 표현식과 조합해서 사용할 대 문제가 발생하는 경우 활용한다.
* noinline
    * 아규먼트로 인라인 함수를 받을 수 없게 만든다.
    * 인라인 함수가 아닌 함수를 아규먼트로 사용하고 싶을 때 활용한다.

```kotlin
inline fun requestNewToken(
    hasToken: Boolean,
    crossinline onRefresh: () -> Unit,
    noinline onGenerate: () -> Unit,
) {
    if (hasToken) {
        httpCall("get-token", onGenerate)
        // 인라인이 아닌 함수를 아규먼트로 함수에 전달하려면 noinline을 사용한다.
    } else {
        httpCall("refresh-token") {
            onRefresh()
            // Non-local 리턴이 허용되지 않는 컨텍스트에서 inline 함수를 사용하고 싶다면 coreeinline을 사용한다.
            onGenerate()
        }
    }
}
```
