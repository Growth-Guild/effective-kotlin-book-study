# Item 33. 생성자 대신 팩토리 함수를 사용하라

- - -

* 디자인 패턴에는 굉장히 다양한 생성 패턴이 있는데, 일반적으로 이러한 생성 패턴은 객체를 생성자로 직접 생성하지 않고, 별도의 함수를 통해 생성한다.
```kotlin
fun <T> myLinkedListOf(
    vararg elements: T,
): MyLinkedList<T>? {
    if (elements.isEmpty()) return null
    val head = elements.first()
    val elementsTail = elemets.copyOfRange(1, elements.size)
    val tail = myLinkedListOf(*elementsTail)
    return MyLinkedList(head, tail)
}
return MyLinkedLIstOf(1, 2)
```
* 생성자의 역할을 대신 해 주는 함수를 팩토리 함수라고 한다.
* 생성자 대신 팩토리 함수를 사용하면 다양한 장점들이 생긴다.
  * 함수에 이름을 붙일 수 있다. 이름은 객체가 생서되는 방법과 아규먼트로 무엇이 필요한지 설명할 수 있다.
  * 원하는 형태의 타입을 리턴할 수 있다. 따라서 다른 객체를 생성할 때 사용할 수 있다. 인터페이스 뒤에 실제 구현 객체를 숨길 때 유용하다.
  * 호출될 때마다 새 객체를 만들 필요가 없다. 함수를 사용해서 객체를 생성하면 싱글턴 패턴처럼 객체를 하나만 생성하게 강제하거나, 최적화를 위해 캐싱 매커니즘을 사용할 수도 있다.
  * 팩토리 함수는 아직 존재하지 않는 객체를 리턴할 수도 있다. 어노테이션 처리를 기반으로 하는 라이브러리에서는 팩토리 함수를 많이 사용한다.
  * 객체 외부에 팩토리 함수를 만들면, 그 가시성을 원하는 대로 제어할 수 있다.
  * 팩토리 함수는 인라인으로 만들 수 있으며, 그 파라미터들을 reified로 만들 수 있다.
  * 팩토리 함수는 생성자로 만들기 복잡한 객체도 만들어 낼 수 있다.
  * 생성자는 즉시 슈퍼클래스 또는 기본 생성자를 호출해야 한다. 하지만 팩토리 함수는 사용하면, 원하는 때에 생성자를 호출할 수 있다.
* 팩토리 함수로 클래스를 생성할 때, 서브클래스 생성에는 슈퍼클래스의 생성자가 필요하기 때문에, 서브클래스를 만들어낼 수 없다.
  * 팩토리 함수로 슈터클래스를 만들기로 했다면, 그 서브클래스에도 팩토리 함수를 만들면 된다.

### Companion 객체 팩토리 함수
* 팩토리 함수를 정의하는 가장 일반적인 방법이다.
* companion 객체는 인터페이스를 구현할 수 있으며, 클래스를 상속받을 수도 있다.
* 추상 companion 객체 팩토리는 값을 가질 수 있다. 따라서 캐싱을 구현하거나, 테스트를 위한 가짜 객체 생성을 할 수 있다.

```kotlin
class MyLinkedList<T>(
  val head: T,
  val tail: MyLinkedList<T>?,
) {
    companion object {
        fun <T> of(vararg elements: T): MyLinkedList<T>? {
            /* ... */
        }
    }
}
```
* 위 코드는 자바에서라면 정적 팩토리 함수와 같다는 것을 알 수 있다.
* 코틀린은 이러한 접근 방법을 인터페이스에도 구현할 수 있다.
```kotlin
class MyLinkedList<T>(
  val head: T,
  val tail: MyLinkedList<T>?
): MyList<T> {
    // ...
}

interface MyList<T> {
    // ...
    
    companion object {
        fun <T> of(vararg elements: T): MyList<T>? {
            // ...
        }
    }
}
```
### 자주 사용하는 팩토리 함수 이름들
* from: 파라미터를 하나 받고, 같은 타입의 인스턴스 하나를 리턴하는 타입 변환 함수를 나타낸다.
```kotlin
val date: Date = Date.from(instant)
```
* of: 파라미터를 여러 개 받고, 이를 통합해서 인스턴스를 만들어 주는 함수
```kotlin
val faceCards: Set<Rank> = EnumSet.of(JACK, QUEEN, KING)
```
* valueOf: from 또는 of 와 비슷한 기능을 하면서도, 의미를 조금 더 쉽게 읽을 수 있게 이름을 붙힌 함수
```kotlin
val prime: BigInteger = BigInteger.valueOf(Integer.MAX_VALUE)
```
* instance 또는 getInstance: 싱글턴으로 인스턴스 하나를 리턴하는 함수. 파라미터가 있을 경우, 아규먼트를 기반으로 하는 인스턴스를 리턴한다.
```kotlin
val luke: StackWalker = StackWalker.getInstance(options)
```
* createInstance 또는 newInstance: getInstance처럼 동작하지만, 싱글턴이 적용되지 않아서, 함수를 호출할 때마다 새로운 인스턴스를 만들어서 리턴한다.
```kotlin
val newArray = Array.newInstance(classObject, arrayLen)
```
* getType: getInstance처럼 동작하지만, 팩토리 함수가 다른 클래스에 있을 때 사용하는 이름이다.
```kotlin
val fs: FileStore = Files.getFileStore(path)
```
* newType: newInstance처럼 동작하지만, 팩토리 함수가 다른 클래스에 있을 때 사용하는 이름이다.
```kotlin
val br: BufferedReader = Files.newBufferedReader(path)
```
- - -

### 확장 팩토리 함수
* 이미 존재하는 companion 객체를 수정할 수 없고, 다른 파일에 함수를 만들어야 할 때 확장 함수를 이용하여 팩토리 함수를 만들면 된다.
```kotlin
fun Tool.Companion.createBigTool(/* ... */) : BigTool {
    // ...
}
Tool.createBigTool()
```
* 다만 companion 객체를 확장하려면, (적어도 비어 있는) Companion 객체가 필요하다.

- - -

### 톱레벨 팩토리 함수
* 객체를 만드는 흔한 방법 중 하나로, 대표적인 예로는 listOf, setOf, mapOf가 있다.
* 하지만 public 톱레벨 함수는 모든 곳에서 사용할 수 있으므로, IDE가 제공하는 팁을 복잡하게 만드는 단점이 있다.
* 톱레벨 함수의 이름을 클래스 메서드 이름처럼 만들면, 다양한 혼란을 일으킬 수 있으므로 이름을 신중하게 생각해서 지정해야 한다.

- - -
### 가짜 생성자
* 코틀린의 생성자는 톱레벨 함수와 같은 형태로 사용된다.
```kotlin
class A
val a = A()
```
* 따라서 다음과 같이 톱레벨 함수처럼 참조될 수 있다. (생성자 레퍼런스는 함수 인터페이스로 구현한다)
```kotlin
val reference: () -> A = ::A
```
* 가짜 생성자는 생성자처럼 작동하면서 팩토리 함수와 같은 모든 장점을 갖는다.
* 진짜 생성자를 대신에 가짜 생성자를 만드는 이유는 다음과 같다.
  * 인터페이스를 위한 생성자를 만들고 싶을 때
  * reified 타입 아규먼트를 갖게 하고 싶을 때
* 이를 제외하면, 가짜 생성자는 진짜 생성자처럼 동작해야 한다.
  * 캐싱, nullable 타입 리턴, 서브클래스 리턴 드으이 기능까지 포함해서 객체를 만들고 싶다면, companion 객체 팩토리 메서드처럼 다른 이름을 가진 팩토리 함수를 사용하는 것이 좋다.
* 가짜 생성자를 선언하는 또 다른 방법은 invoke 연산자를 갖는 companion 객체를 사용하면 된다.
```kotlin
class Tree<T> {
    companion object {
        operator fun <T> invoke(size: Int, generator: (Int) -> T): Tree<T> {
            // ...
        }
    }
}
```
* 다만 위와 같은 방법은 거의 사용되지 않으며, '아이템 12: 연산자 오버로드를 할 때는 의미에 맞게 하라'에 위배되기 때문이다.

- - -

### 팩토리 클래스의 메서드
* 팩토리 클래스는 클래스의 상태를 가질 수 있다는 특정 때문에 팩토리 함수보다 다양한 기능을 갖는다.
* 팩토리 클래스는 프로퍼티를 가질 수 있다.
* 이를 활용하면 다양한 종류로 최적화하고, 다양한 기능을 도입할 수 있다.
  * 캐싱을 활용하거나, 이전에 만든 객체를 복제해서 객체를 생성하는 방법으로 객체 생성 속도를 높일 수 있다.
