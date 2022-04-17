# Item 35. 복잡한 객체를 생성하기 위한 DSL을 정의하라

- - -

* DSL은 복잡한 객체, 계층 구조를 갖고 있는 객체들을 정의할 때 유용하다.

### 사용자 정의 DSL 만들기
* 함수 타입을 만드는 기본적인 방법은 다음과 같다.
  * 람다 표현식
  * 익명 함수
  * 함수 레퍼런스
* Example
```kotlin
fun plus(a: Int, b: Int) = a + b

val plus1: (Int, Int) -> Int = { a, b -> a + b}
val plus2: (Int, Int) -> Int = fun(a, b) = a + b
val plus3: (Int, Int) -> Int = ::plus
```
* 위 예제는 프로퍼티 타입이 지정되어 있으므로, 람다 표현식과 익명 함수의 아규먼트 타입을 추론할 수 있다.
* 반대로 다음과 같이 아규먼트 탕비을 지정해서 함수의 형태를 추론하게 할 수도 있다.
```kotlin
val plus4 = { a: Int, b: Int -> a + b }
val plus5 = fun(a: Int, b: Int) = a + b
```
* 함수 타입은 '함수를 나타내는 객체'를 표현하는 타입이다.
* 익명 확장 함수는 아래와 같이 만들 수 있다.
```kotlin
val myPlus = fun Int.(other: Int) = this + other
```
* 위 예시를 '리시버를 가진 함수 타입'이라고 부른다.
```kotlin
val myPlus2: Int.(Int) -> Int = fun Int.(other: Int) = this + other 
val myPlus3: Int.(Int) -> Int = { this + it }
```
* 위 예시처럼 리시버를 가진 람다 표현식을 사용해서 정의할 수도 있다.
* 스코프 내부에 this 키워드가 확장 리시버를 참조한다.


* 리시버를 가진 익명 확장 함수와 람다 표현식은 다음과 같은 방법으로 호출할 수 있다.
  * 일반적인 객체처럼 invoke 메서드 사용
  * 확장 함수가 아닌 함수처럼 사용
  * 일반적인 확장 함수처럼 사용
```kotlin
myPlus.invoke(1, 2)
myPlus(1, 2)
1.myPlus(2)
```
* 위와 같이 리시버를 가진 함수 타입의 중요한 특징은 this의 참조 대상을 변경할 수 있다는 것이다.

- - -

* 리시버를 가진 함수 타입은 코틀린 DSL을 구성하는 가장 기본적인 블록이다.
```kotlin
fun createTable(): TableDsl = table {
    tr {
        for (i in 1..2) {
            td {
                +"this is column $i"
            }
        }
    }
}
```
* DSL의 앞부분에 table 함수가 있는 것을 볼 수 있다. 현재 코드가 톱레벨에 위치하고, 별도의 리시버를 갖지 않으므로, table 함수도 톱레벨에 있어야 한다.
* 함수 아규먼트 내부에서 tr을 사용하고 있고, tr은 table 정의 내부에서만 허용되어야 한다.
  * table 함수의 아규먼트는 tr 함수를 갖는 리시버를 가져야 한다.
  * tr 함수의 아규먼트는 td 함수를 갖는 리시버를 가져야 한다.
```kotlin
fun table(init: TableBuilder.() -> Unit): TableBuilder {
    val tableBuilder = TableBuilder()
    init.invoke(tableBuilder)
    return tableBuilder
}

class TableBuilder {
    var trs = listOf<TrBuilder>()
    
    fun tr(init: TrBuilder.() -> Unit) {
        trs = trs + TrBuildre().apply(init)
    }
}

class TrBuilder {
    var tds = listOf<TdBuilder>()
    
    fun td(init: TdBuilder.() -> Unit) { 
        tds = tds + TdBuilder().apply(init)
    }
}

class TdBuilder {
    var text = ""
    
    operator fun String.unaryPlus() {
        text += this
    }
}
```
* 'Item 15. 리시버를 명시적으로 참조하라'에서 다루는 DslMarker를 활용하면, 위 코드를 조금 더 좋게 만들 수 있다.

- - -

### 언제 사용해야 할까?
* 복잡한 자료구조를 표현하는 경우
* 계층적인 구조를 표현하는 경우
* 거대한 양의 데이터를 표현하는 경우
