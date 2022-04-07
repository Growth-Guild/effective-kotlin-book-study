# Item 2. 변수의 스코프를 최소화하라.
* 상태를 정의할 때는 변수와 프로퍼티의 스코프를 최소화하는 것이 좋다.
  * 프로퍼티보다 지역 변수를 사용하는 것이 더 좋다.
  * 최대한 좁은 스코프를 갖게 변수를 사용한다.
* 스코프를 좁게 만드는 것이 중요한 이유는 프로그램을 추적하고 관리하기 쉽기 때문이다.
  * 너무 넓은 범위의 스코프는 어떤 시점에 어떤 요소가 있는지 알아야 하는 부담이 커지므로 분석이 어려워진다.
  * 너무 넓은 범위의 스코프는 다른 개발자에 의해 변수가 잘못 사용될 위험이 있다.
* 변수는 읽기 전용 또는 읽고 쓰기 전용 여부와 상관 없이, 변수를 정의할 때 초기화되는 것이 좋다.
  * if, when, try-catch, Elvis 표현식 등을 활용하면, 최대한 변수를 정의할 대 초기화가 가능하다.
* 여러 프로퍼티를 한꺼번에 설정하는 경우에는 구조분해 선언(destructuring declaration)을 활용하는 것이 좋다.
```kotlin
// 나쁜 예
fun updateWeather(degrees: Int) {
    val description: String
    val color: Int
    if (degrees < 5) {
        description = "cold"
        color = Color.BLUE
    } else if (degrees < 23) {
        description = "mild"
        color = Color.YELLOW
    } else {
        description = "hot"
        color = Color.RED
    }
}

// 조금 더 좋은 예
fun updateWeather(degrees: Int) {
    val (description, color) = when {
        degress < 5 -> "cold" to Color.BLUE
        degress < 23 -> "mild" to Color.YELLOW
        else -> "hot" to Color.RED
    }
}
```
- - -

## 캡처링
* 람다는 변수를 캡처하므로 예상 밖으로 변수의 스코프 범위를 넓힐 수 있기 때문에 주의한다.
```kotlin
internal class MainTest {

    @Test
    fun `간단하게 소수 구하는 코드`() {
        var numbers = (2..100).toList()
        val primes = mutableListOf<Int>()
        while (numbers.isNotEmpty()) {
            val prime = numbers.first()
            primes.add(prime)
            numbers = numbers.filter { it % prime != 0 }

        }
        println(primes)   // [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]
    }

    @Test
    fun `시퀀스를 이용하여 소수를 구하는 코드`() {
        val primes: Sequence<Int> = sequence {
            var numbers = generateSequence(2) { it + 1 }

            while (true) {
                val prime = numbers.first()
                yield(prime)
                numbers = numbers.drop(1).filter { it % prime != 0 }
            }
        }

        println(primes.take(10).toList())
        // [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
    }

    @Test
    fun `잘못된 캡처링을 이용하여 소수를 구하는 코드`() {
        val primes = sequence {
            var numbers = generateSequence(2) { it + 1 }

            var prime: Int
            while (true) {
                prime = numbers.first()
                yield(prime)
                numbers = numbers.drop(1)
                    .filter { it % prime != 0 }
            }
        }

        println(primes.take(10).toList())
        // [2, 3, 5, 6, 7, 8, 9, 10, 11, 12]
        /**
         * prime 이라는 변수를 캡처했기 때문에 반복문 내부에서 filter 를 활용해서 prime 으로 나눌 수 있는 숫자를 필터링한다.
         * 하지만 시퀀스를 활용하므로 필터링이 지연되고, 최종적인 prime 값으로만 필터링된다. prime 이 2로 설정되어 있을 때 필터링된 4를 제외하면,
         * drop 만 동작하므로 그냥 연속된 숫자가 나오게 된다.
         */
    }
}
```