# Item 34. 기본 생성자에 있는 옵션 아규먼트를 사용하라

- - -

* 객체를 정의하고 생성하는 방버을 지정할 때 사용하는 가장 기본적인 방법은 기본 생성자(primary constructor)를 사용하는 것이다.

```kotlin
class User(var name: String, var surname: String)

val user = User("Hello", "World")
```

* 프로퍼티는 기본 생성자로 초기화되어도, 디폴트 값을 기반으로 초기화되어도, 어떻게든 초기화만 되면 큰 문제가 없다.
* 기본 생성자가 좋은 방식인 이유를 이해하려면, 생성자와 관련된 자바 패턴들을 이해하는 것이 좋다.
    * 점층적 생성자 패턴
    * 빌더 패턴

- - -

### 점층적 생성자 패턴

* 여러 가지 종류의 생성자를 사용하는 간단한 패턴이다.

```kotlin
class Pizza {
    val size: String
    val cheese: Int
    val olives: Int
    val bacon: Int

    constructor(size: String, cheese: Int, olives: Int, bacon: Int) {
        this.size = size
        this.cheese = cheese
        this.olives = olives
        this.bacon = bacon
    }

    constructor(size: String, cheese: Int, olives: Int) : this(size, cheese, olives, 0)
    constructor(size: string, cheese: Int) : this(size, cheese, 0)
    constructor(size: String) : this(size, 0)
}
```

* 위 코드는 그렇게 좋은 코드는 아니다. 코틀린에서는 일반적으로 다음과 같은 디폴트 아규먼트를 사용한다.

```kotlin
class Pizza(
    val size: String,
    val cheese: Int = 0,
    val olives: Int = 0,
    val bacon: Int = 0,
)
```

* 위와 같이 디폴트 아규먼트는 코드를 단순하고 깔끔하게 만들어 줄 뿐만 아니라, 점층적 생성자보다 훨씬 다양한 기능을 제공한다.
* 예를 들어 size와 olives를 다음과 같은 형태로 지정할 수 있다.

```kotlin
val myFavorite1 = Pizza("L", olives = 3)
val myFavorite2 = Pizza("L", olives = 3, cheese = 1)
```

* 점층적 생성자보다 디폴트 아규먼트가 좋은 이유
    * 파라미터들의 값을 원하는 대로 지정할 수 있다.
    * 아규먼트를 원하는 순서로 지정할 수 있다.
    * 명시적으로 이름을 붙여서 아규먼트를 지정하므로 의미가 훨씬 명확하다.

### 빌더 패턴

* 자바에서는 이름 있는 파라미터와 디폴트 아규먼트를 사용할 수 없다. 그래서 빌더 패턴을 사용한다.
* 빌더 패턴은 다음과 같은 장점이 있다.
    * 파라미터에 이름을 붙일 수 있다.
    * 파라미터를 원하는 순서로 지정할 수 있다.
    * 디폴트 값을 지정할 수 있다.

```kotlin
class Pizza private constructor(
    val size: String,
    val cheese: Int,
    val olives: Int,
    val bacon: Int,
) {
    class Builder(private val size: String) {
        private var cheese: Int = 0
        private var olives: Int = 0
        private var bacon: Int = 0

        fun setCheese(value: Int): Builder = apply {
            cheese = value
        }

        fun setOlives(value: Int): Builder = apply {
            olives = value
        }

        fun setBacon(value: Int): Builder = apply {
            bacon = value
        }

        fun build() = Pizza(size, cheese, olives, bacon)
    }
}

val myFavorite = PizzaBuilder("L")
    .setOlives(3)
    .build()
```

* 빌더 패턴을 사용하는 것보다 이름 있는 파라미터를 사용하는 것이 좋은 이유
    * 더 짧다.
    * 더 명확하다.
    * 더 사용하기 쉽다.
    * 동시성과 관련된 문제가 없다. 코틀린의 함수 파라미터는 항상 immutable 이다. 반면 대부분의 빌더패턴에서 프로퍼티는 mutable 이다. 따라서 빌터 패턴의 빌더 함수를 쓰레드 안전하게 구현하는
      것은 어렵다.

### 빌더 패턴을 사용하면 좋은 경우

* 빌더 패턴은 값의 의미를 묶어서 지정할 수 있다. 또한 특정 값을 누적하는 형태로 사용할 수 있다. (addRoute)

```kotlin
val dialog = AlertDialog.Builder(context)
    .setMessage(R.string.fire_missiles)
    .setPositiveButton(R.string.fire, { d, id ->
        // do somethings..
    })
    .setNegativeButton(R.string.cancel, { d, id ->
        // do somethings..
    })
    .create()

val router = Router.Builder()
    .addRoute(path = "/home", ::showHome)
    .addRoute(path = "/users", ::showUsers)
    .build()
```

* 빌더 패턴을 사용하지 않고 이를 구현하려면, 추가적인 타입들을 만들고 활용해야 하므로 코드가 오히려 복잡해진다.

```kotlin
val dialog = AlertDialog(
    context,
    message = R.string.fire_missiles,
    positiveButtonDescription = ButtonDescription(R.string.fire, { d, id ->
        // do somethings..
    }),
    negativeButtonDescription = ButtonDescription(R.string.cancel, { d, id ->
        // do somethings..
    })
)

val router = Router(
    routes = listOf(
        Route("/home", ::showHome),
        Route("/users", ::showUsers)
    )
)
```

* 위와 같은 코드는 좋게 받아 들여지지 않는다. 일반적으로 이런 코드는 다음과 같이 DSL 빌더를 사용한다.

```kotlin
val dialog = context.alert(R.string.fire_missile) {
    positiveButton(R.string.fire) {
        // do somethings..
    }
    negativeButton {
        "/home" directsTo ::showHome
        "/usres" directsTo ::showUsers
    }
}
```
* 위와 같이 DSL 빌더를 활용하는 패턴이 전통적인 빌더 패턴보다 훨씬 유연하고 명확하다.
