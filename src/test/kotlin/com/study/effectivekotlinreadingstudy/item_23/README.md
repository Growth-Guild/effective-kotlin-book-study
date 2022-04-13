# Item 23. 타입 파라미터의 섀도잉을 피하라

- - -

* 아래와 같이 프로퍼티와 파라미터가 같은 이름을 가지면서 지역 파라미터가 외부 스코프에 있는 프로퍼티를 가리는 것을 섀도잉이라고 한다.
```kotlin
class Forest(val name: String) {
    
    fun addTree(name: String) {
        // ...
    }
}
```
* 섀도잉 현상은 클래스 타입 파라미터와 함수 타입 파라미터 사이에서도 발생한다.
```kotlin
interface Tree
class Birch : Tree
class Spruce: Tree

class Forest<T: Tree> {
    
    fun <T: Tree> addTree(tree: T) {
        // ...
    }
}

val forest = Forest<Birch>()
forest.addTree(Birch())
forest.addTree(Spruce())
```
* 위와 같이 코드를 작성하는 경우, Forest와 addTree의 타입 파라미터가 독립적으로 동작한다.
* 만약 독립적인 타입 파라미터를 의도했다면, 이름을 아예 다르게 다는 것이 좋다.
* 따라서 아래와 같이 addTree가 클래스 타입 파라미터인 T를 사용하게 하는 것이 좋다.
```kotlin
class Forest<T: Tree> {
    fun <ST: T> addTree(tree: ST) {
        // ...
    }
}
```
