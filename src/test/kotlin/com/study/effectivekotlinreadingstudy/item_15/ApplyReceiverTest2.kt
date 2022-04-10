package com.study.effectivekotlinreadingstudy.item_15

import org.junit.jupiter.api.Test

class ApplyReceiverTest2 {

    @Test
    fun `Created parent-child 가 출련된다`() {
        val node = Node("parent")
        node.makeChild("child")
        // Created parent-child
    }

    private class Node(val name: String) {

        fun makeChild(childName: String) =
            create("$name-$childName")
                .apply { print("Created ${this?.name}") }

        fun create(name: String): Node? = Node(name)
    }
}
