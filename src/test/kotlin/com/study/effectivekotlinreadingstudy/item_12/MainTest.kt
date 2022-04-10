package com.study.effectivekotlinreadingstudy.item_12

import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun `infix를 활용하면 연산자의 의미를 분명하게 할 수 있다`() {
        3 * { print("Hello") }  // HelloHelloHello

        println()

        val tripledHello = 3 timesRepeated { print("Hello") }
        tripledHello()  // HelloHelloHello
    }

    operator fun Int.times(operation: () -> Unit) {
        repeat(this) { operation() }
    }

    infix fun Int.timesRepeated(operation: () -> Unit) = {
        repeat(this) { operation() }
    }
}
