package com.study.effectivekotlinreadingstudy.item_2

import org.junit.jupiter.api.Test

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
