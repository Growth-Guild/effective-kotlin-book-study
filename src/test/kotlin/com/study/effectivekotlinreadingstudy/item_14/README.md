# Item 14. 변수 타입이 명확하지 않은 경우 확실하게 지정하라

- - -

* 코틀린은 개발자가 타입을 지정하지 않아도, 타입을 추론해서 지정해주는 타입 추론 시스템을 갖추고 있다.
* 하지만 유형이 명확하지 않을 때는 남용하지 않는 것이 좋다.
```kotlin
val data = getSomeData()
```
* 위의 코드는 타입을 숨기고 있는데, 가독성을 위해 코드를 설계할 대는 읽는 사람에게 중요한 정보를 숨겨서는 안된다.
* 코드를 읽으면서 함수 정의를 보고 타입을 확인해야 하는 번거로움이 코드의 가독성을 해친다.
* 가독성 향상 외에 안전을 위해서도 타입을 지정하는 것이 좋다.
  * 이는 Item 4의 'inferred 타입으로 리턴하지 말라'를 참조한다.
