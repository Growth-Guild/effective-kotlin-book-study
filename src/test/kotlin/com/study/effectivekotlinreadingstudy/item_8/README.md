# Item 8. 적절하게 null을 처리하라

- - -

* String.toIntOrNull()은 String을 Int로 적절하게 변환할 수 없을 경우 null을 리턴한다.
* Iterable<T>.firstOrNull(() -> Boolean)은 주어진 조건에 맞는 요소가 없을 경우 null을 리턴한다.
* 위와 같이 null은 최대한 명확한 의미를 갖는 것이 좋다.
* 기본적으로 nullable 타입은 세 가지 방법으로 처리한다.
    * ?., 스마트 캐스팅, Elvis 연산자 등을 활용해서 안전하게 처리한다.
    * 오류를 throw한다.
    * 함수 또는 프로퍼티를 리팩터링해서 nullable 타입이 나오지 않게 바꾼다.

- - -

### null을 안전하게 처리하기

* 안전 호출(safe call)과 스마트 캐스팅은 null을 안전하게 처리하기에 굉장히 편리하며, 일반적으로 가장 많이 활용한다.

```kotlin
printer?.print()  // 안전 호출
if (printer != null) printer.print()  // 스마트 캐스팅
```

* Elvis 연산자는 오른쪽에 return 또는 throw를 포함한 모든 표현식이 허용된다.

```kotlin
val printerName1 = printer?.name ?: "Unnamed"
val printerName2 = printer?.name ?: return
val printerName3 = printer?.name ?: throw Error("Printer must be named")
```

### 오류 throw하기

* 개발자가 예상하는 것과 다르게 동작할 수 있는 부분에서 문제가 발생할 경우에는 오류를 강제로 발생시켜 주는 것이 좋다.
* 오류를 강제로 발생시킬 때는 throw, !!, requireNotNull, checkNotNull 등을 활용한다.

### not-null assertion(!!)과 관련된 문제

* nullable을 처리하는 가장 간단한 방법은 not-null assertion(!!)을 사용하는 것이다.
    * 하지만 !!을 사용하면 자바에서 nullable을 처리할 때 발생할 수 있는 문제가 똑같이 발생한다. !!은 사용하기 쉽지만 좋은 방법은 아니다.
    * 어떤 대상이 null이 아니라고 생각하고 다루면, NPE 예외가 발생할 수 있다.
    * !!은 nullability에 대한 정보를 숨기므로 개발자가 놓치기 쉽다.
* 일반적으로 nullability가 제대로 표현되지 않는 라이브러리를 사용할 때 정도에만 사용해야 한다. 일반적으로는 !! 사용을 피하는 것이 좋다.

### 의미 없는 nullability 피하기

* nullability는 어떻게든 적절하게 처리해야 하므로, 추가 비용이 발생한다. 따라서 필요한 경우가 아니라면, nullability 자체를 피하는 것이 좋다.
* null은 개발자에게 중요한 메시지를 전달하는 데 사용될 수 있으므로, 다른 개발자가 보기에 의미가 없다면 null을 사용하지 않는 것이 좋다.
* nullability를 피할 때 사용할 수 있는 방법
    * 클래스에서 nullability에 따라 여러 함수를 만들어서 제공할 수도 있다. 대표적으로 List<T>와 get과 getOrNull 함수가 있다.
    * 어떤 값이 클래스 생성 이휴에 확실하게 설정된다는 보장이 있다면, lateinit 프로퍼티와 notNull 델리게이트를 사용한다.
    * 빈 컬렉션 대신 null을 리턴하지 않는다.
    * nullable enum과 None enum 값은 완전히 다른 의미이다. null enum은 별도로 처리가 필요하지만, None enum 정의에 없으므로 필요한 경우에 사용하는 쪽에서 추가해서 활용할 수
      있다는 의미이다.

### lateinit 프로퍼티와 notNull 델리게이트

* lateinit 한정자는 프로퍼티가 이후에 설정될 것임을 명시하는 한정자이다.

```kotlin
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserControllerTest {
    private lateinit var dao: UserDao
    private lateinit var controller: UserController

    @BeforeEach
    fun init() {
        dao = mockk()
        controller = UserController(dao)
    }

    @Test
    fun test() {
        controller.doSomething()
    }
}
```

* lateinit을 초기화 전에 값을 사용하려고 하면 예외가 발생한다.
* lateinit은 nullable과 비교해서 다음과 같은 차이가 있다.
    * !! 연산자로 언팩(unpack)하지 않아도 된다.
    * 이후에 어떤 의미를 나타내기 위해서 null을 사용하고 싶을 때, nullable로 만들 수도 있다.
    * 프로퍼티가 초기화된 이후에는 초기화되지 않은 상태로 돌아갈 수 없다.
* lateinit은 프로퍼티를 처음 사용하기 전에 반드시 초기화될 거라고 예상되는 상황에서 사용한다.
* JVM에서 Int, Long, Double, Boolean과 같은 기본 타입과 연결된 타입으로 프로퍼티를 초기화해야 하는 경우에는 lateinit 보다는 약간 느리지만, Delegates.notNull을
  사용한다.

```kotlin
class DoctorActivity() {
    private var doctorId: Int by Delegates.notNull()
    private var fromNotification: Boolean by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doctorId = intent.extras.getInt(DOCTOR_ID_ARG)
        fromNotification = intent.extras.getBoolean(FROM_NOTIFICATION_ARG)
    }
}
```

* 위 코드처럼 onCreate 때 초기화하는 프로퍼티는 지연 초기화하는 형태로 다음과 같이 프로퍼티 위임(property delegation)을 사용할 수도 있다.

```kotlin
class DoctorActivity : Activity() {
    private var doctorId: Int by arg(DOCTOR_ID_ARG)
    private var fromNotification: Boolean by arg(FROM_NOTIFICATION)
}
```
