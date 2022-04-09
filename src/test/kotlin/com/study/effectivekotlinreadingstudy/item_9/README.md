# Item 9. use를 사용하여 리소스를 닫아라

- - -

* close 메서드를 사용해 명시적으로 닫아야 하는 리소스가 있다.
    * InputStream과 OutputStream
    * java.sql.Connection
    * java.io.Reader(FileReader, BufferedReader, CSSParser)
    * java.new.Socket과 java.util.Scanner
    * etc..
* 위와 같은 리소스들은 AutoCloseable을 상속받는 Closeable 인터페이스를 구현하고 있다.
* 이러한 리소스들은 최종적으로 리소스에 대한 레퍼런스가 없어질 때, 가비지 컬렉터가 처리한다.
* 그동안 리소스를 유지하는 비용이 많이 들어가는데, 필요하지 않다면 명시적으로 close 메서드를 호출해 주는 것이 좋다.
* 전통적인 방법으로는 try-finally 블록을 사용해서 처리했지만 use 함수를 사용해서 간결하게 구현할 수 있다.

```kotlin
// 전통적인 try-finally 구현 방법
fun countCharactersInFile(path: String): Int {
    val reader = BufferedReader(FileReader(path))
    try {
        return reader.lineSequence().sumBy { it.length }
    } finally {
        reader.close()
    }
}

// use를 이용한 구현 방법
fun countCharactersInFile(path: String): Int {
    BufferedReader(FileReader(path)).use { reader ->
        return reader.lineSequence().sumBy { it.length }
    }
}
```

* 파일을 리소스로 사용하는 경우가 많고, 파일을 한 줄씩 읽어 들이는 경우도 많으므로, 코틀린 표준 라이브러리는 파일을 한 줄씩 처리할 때 활용할 수 있는 useLines 함수도 제공한다.
```kotlin
fun countCharactersInFile(path: String): Int {
    File(path).useLines { lines ->
        return lines.sumBy { it.length }
    }
}
```
* 이렇게 처리하면 메모리에 파일의 내용을 한 줄씩만 유지하므로, 대용량 파일도 적절하게 처리할 수 있다.
p