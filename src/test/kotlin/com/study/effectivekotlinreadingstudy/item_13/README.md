# Item 13. Unit?을 리턴하지 말라

- - -

```kotlin
// Boolean 을 반환하는 코드
fun keyIsCorrect(key: String): Boolean {
    // ...
}

if (!keyIsCorrect(key)) return
```
```kotlin
// Unit?을 반환하는 코드
fun verifyKey(key: String): Unit? {
    // ...
}

verifyKey(key) ?: return
```
* Unit?으로 Boolean을 표현하는 것은 오해의 소지가 있으며, 예측하기 어려운 오류를 만들 수 있다.
```kotlin
getData()?.let{ view.showData(it) } ?: view.showError()
```
* 위 코드는 showData가 null을 리턴한다.
* getData가 null이 아닌 값을 리턴할 때는 showData와 showError가 모두 호출된다.
* 이런 코드보다는 if-else 조건문을 사용하는 것이 훨씬 이해하기 쉽다.
* 기본적으로 Unit?을 리턴하거나, 이를 기반으로 연산하지 않는 것이 좋다.
