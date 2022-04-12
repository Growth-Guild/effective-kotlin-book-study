package com.study.effectivekotlinreadingstudy.item_21

import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty

class MainTest {

    @Test
    fun `by 키워드를 이용한 프로퍼티 위임`() {
        var token: String? by LoggingProperty(null)
        var attempts: Int by LoggingProperty(0)

        token = "hello"
        attempts = 5
        token
        attempts
    }

    @Test
    fun `Map의 확장함수를 이용한 프로퍼티 위임`() {
        val map: Map<String, Any> = mapOf(
            "name" to "Marcin",
            "kotlinProgrammer" to true
        )
        val name by map
        val kotlinProgrammer by map
        println(name)
        println(kotlinProgrammer)
    }

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
}
