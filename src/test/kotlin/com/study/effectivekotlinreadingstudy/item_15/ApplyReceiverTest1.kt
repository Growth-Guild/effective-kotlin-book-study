package com.study.effectivekotlinreadingstudy.item_15

import org.junit.jupiter.api.Test

class ApplyReceiverTest1 {

    @Test
    fun `Created parent 가 출련된다`() {
        val node = Node("parent")
        node.makeChild("child")
        // Created parent
    }

    private class Node(val name: String) {

        fun makeChild(childName: String) =
            create("$name.$childName")
                .apply { print("Created ${name}") }

        fun create(name: String): Node? = Node(name)
    }
}
