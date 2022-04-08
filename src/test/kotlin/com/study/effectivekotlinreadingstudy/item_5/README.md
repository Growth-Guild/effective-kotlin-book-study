# Item 5. 예외를 활용해 코드에 제한을 걸어라
- - -

* 코틀린에서는 코드의 동작에 제한을 걸 때 다음과 같은 방법을 사용할 수 있다.
  * require 블록: argument를 제한할 수 있다.
  * check 블록: 상태와 관련된 동작을 제한할 수 있다.
  * assert 블록: 어떤 것이 true인지 확인할 수 있다. assert 블록은 테스트 모드에서만 동작한다.
  * return 또는 throw와 함께 활용하는 Elvis 연산자
```kotlin
// 위 설명의 메커니즘을 사용하는 예시
// Stack<T>의 일부
fun pop(num: Int = 1): List<T> {
    require(num <= size) {
        "Cannot remove more elements than current size"
    }
    check(isOpen) { "Cannot pop from closed stack" }
    val ret = collection.take(num)
    collection = collection.drop(num)
    assert(ret.size == num)
    return ret
}
```
* 제한을 걸면 생기는 장점
  * 문서를 읽지 않은 개발자도 문제를 확인할 수 있다.
  * 문제가 있을 경우에 함수가 예상하지 못한 동작을 하지 않고 예외를 throw하기 때문에 바로 피드백을 받을 수 있다.
  * 코드가 어느정도 자체적으로 검사된다.
  * 스마트 캐스트 기능을 활용할 수 있게 되므로, 캐스트를 적게 할 수 있다.
- - -

## Argument
* require 함수는 조건을 만족하지 못할 때 무조건적으로 IllegalArgumentException을 발생시키므로 제한을 무시할 수 없다.
  * 일반적으로 이러한 처리는 함수의 가장 앞부분에 하게 되므로, 코드를 읽을 때 쉽게 확인할 수 있다.

## 상태
* 어떤 구체적인 조건을 만족할 때만 함수를 사용할 수 있게 해야 할 때 일반적으로 check 함수를 사용한다.
* check 함수는 require과 비슷하지만, 지정된 예측을 만족하지 못할 때, IllegalStateException을 throw한다.
* 상태가 올바른지 확인할 때 사용한다.
* 함수 전체에 대한 어떤 예측이 있을 때는 일반적으로 require 블록 뒤에 배치하여 check를 나중에 한다.
* 사용자가 코드를 제대로 사용할 거라고 믿고 있는 것보다는 항상 문제 상황을 예측하고, 문제 상황에 예외를 throw하는 것이 좋다.

## Assert
* assert는 코틀린/JVM에서만 활성화되며, -eaJVM 옵션을 활성화해야 확인할 수 있다.
* 프로덕션 환경에서는 오류가 발생하지 않는다.
* 테스트 할 때만 활성화되므로, 오류가 발생해도 사용자가 알아차릴 수 없기 때문에 심각한 결과를 초래할 수 있는 경우에는 check를 사용하는 것이 좋다.
* 단위테스트 대신 assert를 활용하면 생기는 장점
  * Assert 계열의 함수는 코드를 자체 점검하며, 더 효율적으로 테스트할 수 있게 해준다.
  * 특정 상황이 아닌 모든 상황에 대한 테스트를 할 수 있다.
  * 실행 시점에 정확하게 어떻게 되는지 확인할 수 있다.
  * 실제 코드가 더 빠른 시점에 실패하게 만든다.
* assert를 활용해도 여전히 단위 테스트는 중요하다.

## nullability와 스마트 캐스팅
* 코틀린에서 require와 check 블록을 통해 어떤 조건을 확인해서 true가 나왔다면, 해당 조건은 이후로도 true일 거라고 가정한다.
* 따라서 이를 활용해서 타입을 비교했다면 스마트 캐스트가 작동한다.
```kotlin
class Person(val email: String?)

fun sendEmail(person: Person, message: String) {
    require(person.email != null)
    val email: String = person.email
    // ...
}
```
* 이런 경우 requireNotNull, checkNotNull이라는 특수한 함수를 사용해도 좋다. (둘다 스마트 캐스트를 지원하므로 변수를 언팩(unpack)하는 용도로 활용할 수 있다.)
```kotlin
class Person(val email: String?)
fun validateEmail(email: String) { /*...*/ }

fun sendEmail(person: Person, text: String) {
    val email = requireNotNull(person.email)
    validateEmail(email)
    // ...
}

fun sendEmail(person: Person, text: String) {
    requireNotNull(person.email)
    validateEmail(person.email)
}
```
* nullability를 목적으로, 오른쪽에 throw 또는 return을 두고 Elvis 연산자를 활용하면 가독성을 더할 수 있다.
```kotlin
fun sendEmail(person: Person, text: String) {
    val email: String = person.email ?: return
    // ...
}
```
* 프로퍼티에 문제가 있어서 null일 때 여러 처리를 해야하는 경우에는 return/throw와 run 함수를 조합해서 활용하면 좋다.
```kotlin
fun sendEmail(person: Person, text: String) {
    val email: String = person.email ?: run {
        log("Email not send, no email address")
        return
    }
    // ...
}
```
