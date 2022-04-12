# Item 21. 일반적인 프로퍼티 패턴은 프로퍼티 위임으로 만들어라

- - -

* 코틀린은 코드 재사용과 관련해서 프로퍼티 위임이라는 기능을 제공한다.
* 프로퍼티 위임을 사용하면 일반적인 프로퍼티의 행위를 추출해서 재사용할 수 있다.
    * 대표적인 예로 lazy 프로퍼티가 있다.
    * stdlib는 lazy 프로퍼티 패턴을 쉽게 구현할 수 있게 lazy 함수를 제공한다.

```kotlin
val value by lazy { createValue() }
```

* 프로퍼티 위임을 사용하면 변화가 있을 때 이를 감지하는 Observable 패턴을 쉽게 만들 수 있다.
    * 다음과 같이 stdlib의 observable 델리게이트를 기반으로 간단하게 구현이 가능하다.

```kotlin
var items: List<Item> by Delegates.observle(listOf()) { _, _, _ ->
    notifyDataSetChanged()
}

var key: String? by Delegates.observable(null) { _, old, new ->
    Log.e["key changed from $old to $new"]
}
```

- - -

* 프로퍼티 위임을 사용하면 타입은 다르지만, 내부적으로 거의 같은 처리를 하거나 자주 반복될 패턴을 추출해낼 수 있다.

```kotlin
var token: String? by LoggingProperty(null)
var attempts: Int by LoggingProperty(0)

private class LoggingProperty<T>(var value: T) {
    operator fun getValue(
        thisRef: Any?,
        prop: KProperty<*>
    ): T {
        println("${prop.name} returned value $value")
        return value
    }

    operator fun setValue(
        thisRef: Any?,
        prop: KProperty<*>,
        newValue: T,
    ) {
        val name = prop.name
        println("$name changed from $value to $newValue")
        value = newValue
    }
}
```

* by가 컴파일되면 아래와 비슷한 형태로 컴파일된다.

```kotlin
@JvmField
private val 'token$delegate' =
    LoggingProperty<String>(null)
var token: String?
    get() = 'token$delegate'.getValue(this, ::token)
    set(value) {
        'token$delegate'.setValue(this, ::token, value)
    }
```

* 프로퍼티 위임은 단순히 값만 처리하는 것이 아니라 컨텍스트(this)와 레퍼런스의 경계도 함께 사용하는 형태로 변한다.
    * 프로프티에 대한 레퍼런스는 이름, 어노테이션과 관련된 정보 등을 얻을 때 사용된다.
    * 컨텍스트는 함수가 어떤 위치에서 사용되는지와 관련된 정보를 제공한다.
    * 이러한 정보들로 인해서 getValue와 setValue 메서드가 여러 개 있어도 컨텍스트를 활용하므로, 상황에 따라서 적절한 메서드가 선택된다.

- - -

* 객체를 프로퍼티 위임하려면 val의 경우 getValue 연산, var의 경우 getValue와 setValue 연산이 필요하다.
* 이러한 연산은 멤버 함수로도 만들 수 있지만, 확장 함수로도 만들 수 있다.

```kotlin
val map: Map<String, Any> = mapOf(
    "name" to "Marcin",
    "kotlinProgrammer" to true
)
val name by map
val kotlinProgrammer by map
println(name)   // Marcin
println(kotlinProgrammer)   // true
```

* 위의 예시는 코틀린에서 stdlib에 다음과 같은 확장 함수가 정의되어 있기 때문에 사용할 수 있는 것이다.

```kotlin
inline operator fun <V, V1 : V> Map<in String, V>.getValue(
    thisRef: Any?,
    property: KProperty<*>
): V1 = getOrImplicitDefault(property.name) as V1
```

- - -
* kotlin stdlib에서 다음과 같은 프로퍼티 델리게이터를 알아두면 좋다.
  * lazy
  * Delegates.observable
  * Delegates.vetoable
  * Delegates.notNull
