# Item 24. 제네릭 타입과 variance 한정자를 활용하라

- - -

```kotlin
class Cup<T>
```
* 위 클래스의 타입 파라미터 T는 variance 한정자(out 또는 in)가 없으므로, 기본적으로 invariant(불공변성)이다.
* variance는 제네릭 타입으로 만들어지는 타입들이 서로 관련성이 없다는 의미이다.
  * 예를 들어 Cup<Int>, Cup<Number>, Cup<Any>, Cup<Nothing>은 어떠한 관련성도 갖지 않는다.
* 만약 어떤 관련성을 원한다면, out 또는 in이라는 variance 한정자를 붙인다.
  * out은 타입 파라미터를 covariant(공변성)로 만든다. 이는 A가 B의 서브 타입일 때, Cup\<A\>가 Cup\<B\>의 서브타입이라는 의미이다.
  * in은 반대 의미이다. in 한정자는 타입 파라미터를 contravariant(반공변성)으로 만든다. 이는 A가 B의 서브타입일 때, Cup\<A\>가 Cup\<B\>의 슈퍼타입이라는 의미이다.

- - -

### 함수 타입
* 코틀린 함수 타입의 모든 파라미터 타입은 contravariant이다.
* 모든 리턴 타입은 covariant이다.
* 함수 타입을 사용할 때는 자동으로 variance 한정자가 사용된다.

### variance 한정자의 안전성
* 자바의 배열은 covariant이다. 이는 배열을 기반으로 제네릭 연산자의 정렬 함수 등을 만들기 위해서라고 한다.
* 하지만 자바의 covariant라는 속성 때문에 문제가 발생한다.
```java
class Main {
  public static void main(String[] args) {
    Integer[] numbers = {1, 4, 2, 1};
    Object[] objects = numbers;
    objects[2] = "B";    // 런타임 오류: ArrayStoreException
  }
}
```
* numbers를 Objects[]로 캐스팅해도 실직적으로 타입이 변경되는 것은 아니기 때문에 String 타입을 할당하려고 하면 오류가 발생한다.
* 코틀린은 이러한 결함을 해결하기 위해서 Array를 invariant로 만들었다. 따라서 Array<Int>를 Array<Any> 등으로 바꿀 수 없다.
* 코틀린은 public in 한정자 위치에 covariant 타입 파라미터(out 한정자)가 오는 것을 금지한다.
  * 가시성을 private로 제한하면, 오류가 발생하지 않는다. 객체 내부에서는 업캐스트 객체에 covariant(out 한정자)를 사용할 수 없기 때문이다.
* covariant(out 한정자)는 public out 한정자 위치에서도 안전하므로 따로 제한되지 않는다.
* 안정성의 이유로 생성되거나 노출되는 타입에만 covariant(out 한정자)를 사용한다. 이러한 프로퍼티는 일반적으로 producer 또는 immutable 데이터 홀더에 많이 사용된다.
* 코틀린은 contravariant 타입 파라미터(in 한정자)를 public out 한정자 위치에 사용하는 것을 금지하고 있다.
  * 가시성을 private로 제한하면, 오류가 발생하지 않는다.

- - -

### variance 한정자 위치
* 선언 부분
  * 일반적으로 이 위치에 사용한다.
  * 이 위치에서 사용하면 클래스와 인터페이스 선언에 한정자가 적용된다.
  * 클래스와 인터페이스가 사용되는 모든 곳에 영향을 준다.
```kotlin
// 선언 쪽의 variance 한정자
class Box<out T>(val value: T)
val boxStr: Box<String> = Box("Str")
val boxAny: Box<Any> = boxStr
```
* 클래스와 인터페이스를 활용하는 위치
  * 이 위치에 variance 한정자를 사용하면 특정한 변수에만 variance 한정자가 적용된다.
```kotlin
class Box<T>(val value: T)
val boxStr: Box<String> = Box("str")
// 사용하는 쪽의 variance 한정자
val boxAny: Box<out Any> = boxStr
```
* 모든 인스턴스에 variance 한정자를 적용하면 안 되고, 특정 인스턴스에만 적용해야 할 때 위와 같은 코드를 사용한다.

- - -

* variant 한정자를 사용하면, 위치가 제한될 수 있다.
  * 예를 들어 MutableList<out T>가 있다면, get으로 요소를 추출했을 때 T 타입이 나올 것이다.
  * 하지만 set은 Nothing 타입의 아규먼트가 전달될 거라 예상되므로 사용할 수 없다.
* MutableList<in T>를 사용할 경우
  * get과 set 모두 사용할 수 있다.
  * get은 전달되는 자료형은 Any?가 된다. 이는 모든 타입의 슈퍼타입을 가진 리스트(Any 리스트)가 존재할 가능성이 있기 때문이다.
