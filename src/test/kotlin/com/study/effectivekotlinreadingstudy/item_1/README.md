# Item 1. 가변성을 제한하라
- - -
* 코틀린은 모듈로 프로그램을 설계한다.
  * 모듈은 클래스, 객체, 함수, 타입 별칭(type alias), 톱레벨(top-level) 프로퍼티 등 다양한 요소로 구성되는데, 이 중 일부는 상태(state)를 가질 수 있다.
* 상태를 적절하게 관리하는 것은 어렵다.
  1. 프로그램을 이해하고 디버그하기 힘들어진다.
  2. 가변성(mutability)이 있으면, 코드의 실행을 추론하기 어려워진다.
  3. 멀티스레드 프로그램일 때는 적절한 동기화가 필요하다.
  4. 테스트하기 어렵다.
  5. 상태 변경이 일어날 때, 이러한 변경을 다른 부분에 알려야 하는 경우가 있다. ex) 정렬이 되어 있는 리스트에 가변 요소를 추가하는 경우.
- - -

## 코틀린에서 가변성 제한하기
* 코틀린은 가변성을 제한할 수 있게 설계되어 있다.
  * 읽기 전용 프로퍼티 (val)
  * 가변 컬렉션과 읽기 전용 컬렉션 구분하기
  * 데이터 클래스의 copy

### 읽기 전용 프로퍼티 (val)
* 코틀린은 val을 사용해 읽기 전용 프로퍼티를 만들 수 있지만 완전히 변경 불가능한 것은 아니다.
* 읽기 전용 프로퍼티가 mutable 객체를 담고 있다면 내부적으로 변할 수 있다.
```kotlin
val list = mutableListOf(1, 2, 3)
list.add(4)

println(list) // [1, 2, 3, 4]
```
* 읽기 전용 프로퍼티는 다른 프로퍼티를 활용하는 사용자 정의 게터로도 정의할 수 있다.
```kotlin
var first: String = "Hello"
var second: String = "World"
val fullName
    get() = "$first $second"

fun main() { 
  println(fullName) // Hello World
  name = "Goodbye"
  println(fullName) // Goodbye World
}
```
* 코틀린의 프로퍼티는 기본적으로 캡슐화되어 있고, 추가적으로 사용자 정의 접근자(getter와 setter)를 가질 수 있다.
  * var는 게터와 세터를 모두 제공하짐나 val은 변경이 불가능하므로 게터만 제공한다. 따라서 val을 var로 오버라이드 할 수 있다.
```kotlin
interface Element {
    val active: Boolean
}

class ActualElement : Element {
    override var active: Boolean = false
}
```

### 가변 컬렉션과 읽기 전용 컬렉션 구분하기
* 코틀린에는 읽고 쓸 수 있는 컬렉션과 읽기 전용 컬렉션으로 구분된다.
  * 읽기 전용 인터페이스 : Iterable, Collection, Set, List
  * 읽고 쓸 수 있는 인터페이스 : MutableIterable, MutableCollection, MutableSet, MutableList
* 읽기 전용 컬렉션이 내부의 값을 변경 수 없다는 의미는 아니므로 주의한다.
* 컬렉션의 다운캐스티은 계약을 위반하고, 추상화를 깨버릴 수 있으므로 주의한다.
```kotlin
val list = listOf(1, 2, 3)

if (list is MutableList) {
  list.add(4)
}
```
* 위의 코드는 안전하지 않고, 실행 결과가 플랫폼에 따라 달라진다.
  * JVM에서 listOf는 자바의 List 인터페이스를 구현한 Array.ArrayList 인스턴스를 리턴하고, 이 인터페이스는 add와 set 같은 메서드를 제공한다.
  * 따라서 코틀린의 MutableList로 변경할 수 있는데, 코틀린의 Arrays.ArrayList는 이러한 연산을 구현하고 있지 않다. 따라서 다음과 같은 오류가 발생한다.
```text
Exception in thread "main" java.lang.UnsupportedOperationException
	at java.base/java.util.AbstractList.add(AbstractList.java:153)
	at java.base/java.util.AbstractList.add(AbstractList.java:111)
```
* 따라서 코틀린에서 읽기 전용 컬렉션을 mutable 컬렉션으로 다운캐스팅해서는 안된다.
* 읽기 전용에서 mutable로 변경해야 한다면, 복제(copy)를 통해서 새로운 mutable zjffprtusdmf aksemsms list.toMutableList를 활용한다.

### 데이터 클래스의 copy
* immutable 객체 사용의 장점
  * 한 번 정의된 상태가 유지되므로, 코드를 이해하기 쉽다.
  * 객체를 공유했을 때 충돌이 따로 이루어지지 않으므로, 병렬 처리를 안전하게 할 수 있다.
  * 객체에 대한 참조는 변경되지 않으므로, 쉽게 캐시할 수 있다.
  * 방어적 복사본(defense copy)을 만들 필요가 없다.
  * 다른 객체(mutable 또는 immutable)를 만들 때 활용하기 좋고, 실행을 더 쉽게 예측할 수 있다.
  * set 또는 map의 키로 사용할 수 있다. (mutable 객체는 이러한 것으로 사용할 수 없다.)
* immutable 객체 사용의 단점
  * 변경할 수 없다.
  * 하지만 data 한정자를 통해 해결 가능하다.
  * data 클래스는 copy라는 이름의 메서드를 만들어 준다.
  * copy 메서드는 기본 생성자 프로퍼티가 같은 새로운 객체를 만들어 낸다.
- - -
  
## 다른 종류의 변경 가능 지점
```kotlin
val list1: MutabeList<Int> = mutableListOf()
var list2: List<Int> = listOf()

// 위 코드는 두 가지 모두 변경할 수 있지만 방법이 다르다.

list1.add(1)
list2 = list1 + 1

// 물론 두 가지 코드 모두 다음과 같이 += 연산자를 활용하여 변경할 수 있지만 실질적으로 이루어지는 처리는 다르다.
list1 += 1
list2 += 2
```
* 두 가지 모두 정상적으로 동작하지만 변경 가능 지점의 위치가 다르다.
* 첫 번째 코드는 구체적인 리스트 구현 내부에 변경 가능한 지점이 있다.
  * 멀티스레드 처리가 이루어질 경우, 내부적으로 적절한 동기화가 되어있는지 확실하게 보장할 수 없으므로 위험하다.
* 두 번째 코드는 프로퍼티 자체가 변경 가능한 지점이다.
  * 멀티스레드 처리의 안정성이 더 좋다. (물론 잘못 만들면 일부 요소가 손실될 수도 있다.)

- - -
## 변경 가능 지점 노출하지 말기
* 상태를 나타내는 mutable 객체를 외부에 노출하는 것은 돌발적인 수정이 일어날 때 위험할 수 있다.
* 이를 처리하는 방법은 아래와 같다.
  * 리턴되는 mutable 객체를 복제한다. 이때, data 한정자로 만들어지는 copy 메서드를 활용하면 좋다. - 방어적 복제(defensive copying)
  * 가능하다면 무조건 가변성을 제한하는 것이 좋다. 컬렉션은 객체를 읽기 전용 슈퍼타입으로 업캐스트하여 가변성을 제한할 수도 있다.
