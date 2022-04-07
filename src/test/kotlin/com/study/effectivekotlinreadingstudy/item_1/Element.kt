package com.study.effectivekotlinreadingstudy.item_1

interface Element {
    val active: Boolean
}

class ActualElement : Element {
    override var active: Boolean = false
}

fun main() {
    val list = listOf(1, 2, 3)

    if (list is MutableList) {
        list.add(4)
    }

    println(list)
}
