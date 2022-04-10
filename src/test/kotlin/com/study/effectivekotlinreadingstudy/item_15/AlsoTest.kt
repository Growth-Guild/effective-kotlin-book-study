package com.study.effectivekotlinreadingstudy.item_15

import org.junit.jupiter.api.Test

class AlsoTest {

    @Test
    fun `Created parent-child가 출력된다`() {
        val node = Node("parent")
        node.makeChild("child")
        // Created parent-child
    }

    private class Node(val name: String) {

        fun makeChild(childName: String) =
            create("$name-$childName")
                .also { print("Created ${it?.name}") }

        fun create(name: String): Node? = Node(name)
    }
}
