# Item 15. 리시버를 명시적으로 참조하라

- - -

### 여러 개의 리시버
* 스코프 내부에 둘 이상의 리시버가 있는 경우, 리시버를 명시적으로 나타내면 좋다.
* apply, with, run 함수를 사용할 때가 대표적인 예시이다.
```kotlin
class Node(val name: String) {
    
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply { print("Created ${name}") }
    
    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
```
* 위 코드는 'Created parent.child'가 출력된다고 예상하지만, 실제로는 'Created parent'가 출력된다.
* 이해를 위해 명시적으로 리시버를 붙이면 아래와 같다.
```kotlin
class Node(val name: String) {
    
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply { print("Created ${this.name}") }
            // 컴파일 오류
    
    fun create(name: String): Node? = Node(name)
}
```
* 문제는 apply 함수 내부에서 this 타입이 Node?라서, 이를 직접 사용할 수 없다는 것이다. 이를 사용하기 위해서는 아래와 같이 unpack하고 호출해야 한다.
```kotlin
class Node(val name: String) {
    
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply { print("Created ${this?.name}") }
    
    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
```
* 위의 코드들은 apply의 잘못된 사용 예시이고, also 함수와 파라미터 name을 사용했다면, 이런 문제 자체가 일어나지 않는다.
* 일반적으로 also 또는 let을 사용하는 것이 nullable 값을 처리할 때 훨씬 좋은 선택지이다.
```kotlin
class Node(val name: String) {
    
    fun makeChild(childName: String) =
        create("$name.$childName")
            .also { print("Created ${it?.name}") }
    
    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
```
* 코드를 짧게 할 수 있다는 이유만으로 리시버를 제거하지 않는 것이 좋다.
* 리시버가 명확하지 않다면, 명시적으로 리시버를 적어서 이를 명확하게 해주는 것이 좋다.
* 레이블 없이 리시버를 사용하면, 가장 가까운 리시버를 의미한다. 외부에 있는 리시버를 사용하려면 레이블을 사용해야 한다.

### apply와 also 차이가 뭐지?
```kotlin
public inline fun <T> T.apply(block: T.() -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

public inline fun <T> T.also(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}
```
* apply와 also는 객체를 넘겨 받는 방식이 다르다.
* apply는 파라미터로 받는 람다 리시버에 확장 함수로 처리된다. this를 생략하고 멤버에 접근이 가능하다.
  * 주로 리시버의 멤버를 초기화 할 때 사용한다.
* also는 파라미터로 받는 람다의 인자로 리시버를 전달 받는다. 리시버를 명시적으로 표현해야 한다.
  * 코드 블록 내에서 리시버에 대한 참조가 필요한 작업에 사용한다.
* https://kotlinlang.org/docs/scope-functions.html#apply

- - -

### DSL 마커
* 코틀린 DSL을 사용할 때는 여러 리시버를 가진 요소들이 중첩되더라도, 리시버를 명시적으로 붙이지 않는다. DSL은 원래 그렇게 사용하도록 설계되었기 때문이다.
* 하지만 DSL에서는 외부의 함수를 사용하는 것이 위험한 경우가 있다.
```kotlin
table {
    tr {
        td { +"Column 1" }
        td { +"Column 2" }
    }
    tr {
        td { +"Value 1" }
        td { +"Value 2" }
    }
}
```
* 기본적으로 모든 스코프에서 외부 스코프에 있는 리시버의 메서드를 사용할 수 있지만, 아래와 같은 코드에 문제가 발생한다.
```kotlin
table {
    tr {
        td { +"Column 1" }
        td { +"Column 2" }
        tr {
            td { +"Value 1" }
            td { +"Value 2" }
        }
    }
}
```
* 이러한 잘못된 사용을 막으려면, 암묵적으로 외부 리시버를 사용하는 것을 막는 DslMarker라는 메타 애너테이션(애너테이션을 위한 애너테이션)을 사용한다.
```kotlin
@DslMarker
annotation class HtmlDsl

fun table(f: TableDsl.() -> Unit) { /*...*/ }

@HtmlDsl
class TableDsl { /*...*/ }
```
* 위와 같이 하면 외부 리시버를 사용하는 것이 금지된다.
* 외부 리시버의 함수를 사용하려면, 다음과 같이 명시적으로 해야한다.
```kotlin
table {
    tr {
        td { +"Column 1" }
        td { +"Column 2" }
        this@table.tr {
            td { +"Value 1" }
            td { +"Value 2" }
        }
    }
}
```
* DSL 마커는 가장 가까운 리시버만을 사용하게 하거나, 명시적으로 외부 리시버를 사용하지 못하게 할 때 활용할 수 있는 중요한 메커니즘이다.
