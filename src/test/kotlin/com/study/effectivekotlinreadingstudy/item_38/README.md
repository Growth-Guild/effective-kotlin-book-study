# Item 38. 연산 또는 액션을 전달할 때는 인터페이스 대신 함수 타입을 전달하라

- - -

* 대부분의 프로그래밍 언어에서는 함수 타입이라는 개념이 없기 때문에 연산자 또는 액션을 전달할 때 메서드가 하나만 있는 인터페이스를 활용한다.
* 이러한 인터페이스를 SAM(Single-Abstract Method)이라고 부른다.
* SAM 대신에 함수 타입을 사용하면 더 많은 장점이 있다.

- - -

* 람다 표현식 또는 익명 함수로 전달
```kotiln
setOnClickListner { /* ... */ }
setOnClickListener(fun(view) { /* ... */ }) 
```
* 함수 레퍼런스 또는 제한된 함수 레퍼런스로 전달
```kotlin
setOnClickListener(::println)
setOnClickListener(this::showUsers)
```
* 선언된 함수 타입을 구현한 객체로 전달
```kotlin
class ClickListener: (View) -> Unit {
    override fun invoke(view: View) {
        // ...
    }
}

setOnClickListener(ClickListener())
```
* 타입 별칭(type alias)를 사용하면, 함수 타입도 이름을 붙일 수 있다.
```kotlin
typealias OnClick = (View) -> Unit
```
* 파라미터도 이름을 가질 수 있다.
```kotlin
fun setOnClickListener(listener: OnClick) { /* ... */ }
typealias OnClick = (view: View) -> Unit
```
* 람다 표현식을 사용할 때는 아규먼트 분해(destructure argument)도 사용할 수 있다.
```kotlin
class CalendarView {
    var onDateClicked: ((date: Date) -> Unit)? = null
    var onPageChanged: ((date: Date) -> Unit)? = null
}
```

- - -

### 언제 SAM을 사용해야 할까?
* 코틀린이 아닌 다른 언어에서 사용할 클래스를 설계할 때
